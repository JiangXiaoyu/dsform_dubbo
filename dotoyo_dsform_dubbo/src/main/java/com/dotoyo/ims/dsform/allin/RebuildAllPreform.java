package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotoyo.dsform.dao.inter.IAnnexDao;
import com.dotoyo.dsform.excel.ExcelColumnBo;
import com.dotoyo.dsform.excel.ExcelPictureBo;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.TfPreformService;
import com.dotoyo.dsform.service.inter.IAnnexSv;

/**
 * 重新生成文件模版的html文件a
 * @author wangl
 *
 */
public class RebuildAllPreform implements  Runnable, IThread {
	
	//尽量保证此任务每次只有一个
	private static boolean isBusy = false;
	
	private String rebuildVersion ="";
	
	protected static final transient IFrameLog log= new LogProxy(
			LogFactory.getLog(RebuildAllPreform.class));

	public RebuildAllPreform() {
	}
	
	public RebuildAllPreform(String rebuildVersion) {
		this.rebuildVersion = rebuildVersion;
	}

	@Override
	public void doTask(){
		log.error("########rebuild preform start!!");
		isBusy = true;
		
		//压缩打包，出问题后可以回滚
/*		log.error("########compress start!!");
  		boolean flag = tarFile(filePath);
		if(!flag){
			log.error("########compress error!!");
			log.error("########Thread stop!!");
			isBusy = false;
			return;
		}
		log.error("########compress end!!");*/
		
		//重新生成HTML
		try{
			rebuildAllPreform();
		}catch(Exception e){
			isBusy = false;
			log.error(e);
			log.error("########rebuild preform end!!");
			return;
		}
		log.error("########rebuild preform end!!");
		isBusy = false;
	}

	/**
	 * 重新解析Excel
	 */
	private void rebuildAllPreform() throws Exception {
		IFrameService fs = FrameFactory.getServiceFactory(null);
		ITfPreformService sv = (ITfPreformService) fs.getService(TfPreformService.class.getName());
		
		IAnnexSv annexSv = (IAnnexSv) fs.getService(IAnnexSv.class.getName());
		
		Map<String,String> condition = new HashMap<String,String>();
		condition.put("limit", "100");
		condition.put("rebuildVersion", rebuildVersion);
		condition.put("order", "desc");
		condition.put("sort", "CREATE_TIME");
		
		Date begin = new Date();
		
		//每次处理一百条记录,循环查询
		while(true){
			List<String> idList = sv.selectIds4Limit(condition);
			long sum = sv.countUnrebuilde(condition);
			
			Date end = new Date();
			//如果重新编译时间超过六小时,则退出
			if((end.getTime() - begin.getTime())/(60*60*1000) >6 ){
				log.error("########编译版本"+  rebuildVersion +": 超时!!");
				break;
			}
			
			log.error("########总共还有"+  sum +" 个文件未编译!!");
			//如果没有记录了就退出
			if(idList == null || idList.size() == 0 || sum == 0){
				log.error("########编译版本"+  rebuildVersion +": 重新编译结束!!");
				break;
			}
			Excel2Table eu = new Excel2Table();
			
			for(String id : idList){
				TfPreformModel model = new TfPreformModel();
				model.setId(id);
				model = sv.selectByPrimaryKey(model);
				
				String excelId = model.getFile();
				AnnexModel annex = annexSv.getAnnexById(excelId);
				
				if(annex == null){
					IFrameServiceLog logF = FrameFactory.getServiceLogFactory(null);
					// 处理业务日志
					logF.addServiceLog(IFrameServiceLog.ERROR, this.getClass(),
							"rebuildAllPreform","TfPreformModel id:" + id);
					
					model.setRebuildVersion(rebuildVersion);//设置编译版本
					sv.updateByPrimaryKey(model);
					log.error("########build "+  excelId +" end!!");
					break;
				}
				
				String excelFilePath = annex.getPath() + File.separator + model.getFile();
				boolean is2007 = isExcel2007(excelFilePath);
				
				File file = new File(excelFilePath);
				if(!file.exists() || "auto".equals( model.getThisType())){
					//文件不存在 或 类型为 auto 
				}else if(is2007){
					//2007不管
				}else{
					rebuild2003(eu, model, excelFilePath);
				}
				model.setRebuildVersion(rebuildVersion);//设置编译版本
				sv.updateByPrimaryKey(model);
				log.error("########build "+  excelId +" end!!");
			}
		}
	}

	private void rebuild2003(Excel2Table eu, TfPreformModel model,
			String excelFilePath) {
		String excelId = model.getFile();
		String vPath = model.getVirtualPath();
		try{
			if("multipage".equals(model.getNote())){//处理多页模版
				
				HashMap<String,String> map = new HashMap<String,String>();
				//1.上传固定类型可编辑的文件模版（多页模版）
				//	图片解析出来重用
				List<PictureStyleBo> allPicList = new ArrayList<PictureStyleBo>();
				//	table解析出来重用
				List<ExcelTableBo> tableList = new ArrayList<ExcelTableBo>();
				map.put("path", excelFilePath);//需要excel地址解析多个ExcelTableBo
				map.put("thisType", "fixed");
				map.put("isOldPreform", "new".equals((model.getMethod()))?"false":"true");
				map.put("id", model.getMdfHtml());
				map.put("virtualPath",vPath );//生成目录
				map.put("excelId",excelId );//生成目录
				String mdfId = uploadMdfHtml4Mul( map, tableList, allPicList);
				model.setMdfHtml(mdfId);

				//2.上传固定类型可预览的文件模版（多页模版）
				map.put("thisType", "fixed");
				map.put("id", model.getHtml());
				String hId = uploadViewHtml4Mul( map, allPicList, tableList);
				model.setHtml(hId);
				
				//3.上传自适应类型可预览的文件模版（多页模版）
				map.put("thisType", "auto");
				map.put("id", model.getAutoModelId());
				String aId = uploadViewHtml4Mul( map, allPicList, tableList);
				model.setAutoModelId(aId);
				
				//4.上传手机版可预览的文件模版（多页模版）
				map.put("thisType", "mobile");
				map.put("id", model.getMobileModelId());
				String mId = uploadViewHtml4Mul( map, allPicList, tableList);
				model.setMobileModelId(mId);
			}else{//处理单个模版
				
				HashMap<String,String> map = new HashMap<String,String>();
				//	解析Excel的各个组件
				ExcelTableBo table = eu.toTable(excelFilePath, 0);
				
				table.setOldPreform("new".equals(model.getMethod())?false:true);
				
				//1生成固定类型的可编辑的文件模版  
				//	设置生成的html类型（固定类型）
				
				map.put("id", model.getMdfHtml());
				map.put("virtualPath",vPath );//生成目录
				map.put("excelId",excelId );//生成目录
				
				IExcel2Html dmfHtml = new Excel2MdfHtml();
				String mdfId = uploadHtml4Normal( map,dmfHtml, table);
				model.setMdfHtml(mdfId);
				
				//2生成固定类型的可预览的文件模版  （固定可预览）
				//	可编辑与可预览的文件模版共用图片
				List<PictureStyleBo> picList = table.getPicStyleList();
				IExcel2Html html = new Excel2ViewHtml(picList);
				//	设置生成的html类型（固定类型）
				table.setThisType("fixed");
				map.put("id", model.getHtml());
				String hId = uploadHtml4Normal( map, html, table);
				model.setHtml(hId);
				
				//3生成自适应类型的可预览的文件模版（自适应可预览）
				//	设置生成的html类型（固定类型）
				table.setThisType("auto");
				map.put("id", model.getAutoModelId());
				String aId =uploadHtml4Normal( map, html, table);
				model.setAutoModelId(aId);
				
				//4生成手机端可预览的文件模版（手机端可预览）
				//	设置生成的html类型（固定类型）
				table.setThisType("mobile");
				map.put("id", model.getMobileModelId());
				String mId = uploadHtml4Normal( map, html, table);
				model.setMobileModelId(mId);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(e);
		}
	}
	
	//上传单页文件模版
	private String uploadHtml4Normal(Map<String, String> map, IExcel2Html dmfHtml,
			ExcelTableBo table) throws Exception,
			UnsupportedEncodingException, IOException {
		clearTableData(table);
		
		//生成html页面	
		String mdfHtmlStr = dmfHtml.toHtml(table);
		
		return writeToFile(mdfHtmlStr,  map);
	}

	private String writeToFile(String mdfHtmlStr, Map<String, String> map)
			throws Exception {
		String id = map.get("id");
		
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "html");
		htmlParMap.put("virtualPath", map.get("virtualPath"));//生成目录
		htmlParMap.put("excelId",  map.get("excelId"));//生成目录
		
		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		ByteArrayInputStream mdfByterInputStream = null;
		
		IFrameService svf = FrameFactory.getServiceFactory(null);
		AnnexModel htmlAnnex = new AnnexModel();
		htmlAnnex.setId(id);
		
		IAnnexDao dao = (IAnnexDao) svf.getService(IAnnexDao.class.getName());
		htmlAnnex = dao.selectByPrimaryKey(htmlAnnex);
		
		if(htmlAnnex == null){
			try {
				htmlAnnex = new AnnexModel();
				mdfByterInputStream = new ByteArrayInputStream(mdfHtmlStr.getBytes("utf-8"));
				//这是新增
				iAnnex.upload(htmlAnnex, mdfByterInputStream, htmlParMap);
				iAnnex.commit(htmlAnnex);
			}catch(Exception e){
				log.error(e);
			}finally {
				if (mdfByterInputStream != null) {
					mdfByterInputStream.close();
				}
			}
		}else{
			try {
				mdfByterInputStream = new ByteArrayInputStream(mdfHtmlStr.getBytes("utf-8"));
				//这里是更新
				iAnnex.update(htmlAnnex, mdfByterInputStream, htmlParMap);
				//iAnnex.commit(htmlAnnex); 不需要改状态
			}catch(Exception e){
				log.error(e);
			} finally {
				if (mdfByterInputStream != null) {
					mdfByterInputStream.close();
				}
			}
		}
		return htmlAnnex.getId();
	}
	
	private boolean isExcel2007(String filePath) {
		try {
			new XSSFWorkbook(new FileInputStream(filePath));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private String uploadMdfHtml4Mul(Map<String, String> map,
			List<ExcelTableBo> tableList,List<PictureStyleBo> allPicList) throws FileNotFoundException, IOException,
			InvalidFormatException, Exception, UnsupportedEncodingException {
		int startIdValue = 0;

		String filePath = map.get("path");
		File file = new File(filePath);

		IExcel2Html dmfHtml = new Excel2MdfHtml();
		IExcel2Table eu = new Excel2Table();

		StringBuilder mdfTotalStr = new StringBuilder("");
		boolean hasEditor = false;
		
		InputStream ins = null;
		try{
			ins = new FileInputStream(file);
			Workbook book = WorkbookFactory.create(ins);
			int sheetNum = book.getNumberOfSheets();
			
			List<ExcelPictureBo> picList =null;
			List<PictureStyleBo> picStyList = null;
			for (int index = 0; index < sheetNum; index++) {
				ExcelTableBo table = eu.toTable(filePath,index);
				tableList.add(table);
				table.setOldPreform(map.get("isOldPreform").equals("true")? true : false);
				//设置生成的html类型（固定类型）
				table.setThisType(map.get("thisType"));
				
				//第一个table取出所有图片,其余table不必取图片对象了
				if(index != 0){
					table.setPicList(picList);
					table.setPicStyleList(picStyList);
				}
				
				if(hasEditor){
					table.setHasEditor(hasEditor);//设置全局是否有编辑器
				}
				table.setStartIdValue(startIdValue);//设置控件id的初始值
				String mdfHtmlStr = dmfHtml.toHtml(table);
				
				//第一个table拥有所有图片,其余table不必取图片对象了
				//picStyList必须在 dmfHtml.toHtml(table)之后才有值
				if(index == 0){
					picList = table.getPicList();
					picStyList = table.getPicStyleList();
				}
				
				if ("".equals(mdfTotalStr.toString())) {
					mdfTotalStr.append(mdfHtmlStr);
				} else {
					int bodyStartIndex = mdfHtmlStr.indexOf("<form id='preformForm'>");
					int bodyEndIndex = mdfHtmlStr.indexOf("</form>");
					if (bodyStartIndex != -1 && bodyEndIndex != -1) {
						String bodyContent = mdfHtmlStr.substring(bodyStartIndex + 23, bodyEndIndex);
	
						int startIndex = mdfTotalStr.indexOf("</form>");
						mdfTotalStr.insert(startIndex, bodyContent);
						// 分页打印 +  留白
						//mdfTotalStr.insert(startIndex, "<div style='page-break-after:always'>&nbsp;</div>");
						mdfTotalStr.insert(startIndex, "<!--mulPreform-->");
					}
				}
				if (table.getPicStyleList() != null && table.getPicStyleList().size() != 0) {
					allPicList.addAll(table.getPicStyleList());
				}
				startIdValue += table.getParam().size();//控件id的值
				
				if(table.isHasEditor()){
					hasEditor = true;
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			if(ins != null){
				ins.close();
			}
		}

		return writeToFile(mdfTotalStr.toString(), map);
	}
	
	/*
	 * 数据复原
	 */
	private void clearTableData(ExcelTableBo table){
		table.setParam(new HashMap<String, ExcelColumnBo>());
		List<PictureStyleBo> picList = table.getPicStyleList();
		for(PictureStyleBo pic : picList){
			pic.setViewStatus("0");
			pic.setMdfStatus("0");
		}
		table.setPicStyleList(picList);
	}
	
	private String uploadViewHtml4Mul(Map<String, String> map,
			List<PictureStyleBo> allPicList,
			List<ExcelTableBo> tableList) throws Exception,
			UnsupportedEncodingException, IOException {
		int startIdValue = 0;//控件id的初始值
		IExcel2Html html = new Excel2ViewHtml(allPicList);// 2003
		StringBuilder viewTotalStr = new StringBuilder("");
		
		List<ExcelPictureBo> picList =null;
		List<PictureStyleBo> picStyList = null;
		for (int index = 0; index < tableList.size(); index++) {
			ExcelTableBo table = tableList.get(index);	
			if(map.get("thisType")!=null){
				table.setThisType(map.get("thisType"));
			}

			//第一个table拥有所有图片,其余table不必取图片对象了
			if(index != 0){
				table.getParam().clear();//这里必须要清空,否则控件id会错误
				table.setPicList(picList);
				table.setPicStyleList(picStyList);
			}else{
				clearTableData(table);//第一次进来必须,重置table里的属性(这里是为了解决 取控件id和图片错误问题)
			}
			
			table.setStartIdValue(startIdValue);//设置控件id的初始值
			String viewHtmlStr = html.toHtml(table);
			
			//第一个table拥有所有图片,其余table不必取图片对象了
			//picStyList必须在 dmfHtml.toHtml(table)之后才有值
			if(index == 0){
				picList = table.getPicList();
				picStyList = table.getPicStyleList();
			}
			
			if ("".equals(viewTotalStr.toString())) {
				viewTotalStr.append(viewHtmlStr);
			} else {
				int bodyStartIndex = viewHtmlStr.indexOf("<form id='preformForm'>");
				int bodyEndIndex = viewHtmlStr.indexOf("</form>");
				if (bodyStartIndex != -1 && bodyEndIndex != -1) {
					String bodyContent = viewHtmlStr.substring(bodyStartIndex + 23, bodyEndIndex);

					int startIndex = viewTotalStr.indexOf("</form>");
					viewTotalStr.insert(startIndex, bodyContent);
					// 分页打印 +  留白
					//viewTotalStr.insert(startIndex, "<div style='page-break-after:always'>&nbsp;</div>");
					viewTotalStr.insert(startIndex, "<!--mulPreform-->");
				}
			}
			startIdValue += table.getParam().size();//控件id的值
		}
	
		return writeToFile(viewTotalStr.toString(), map);
	}

	@SuppressWarnings("unused")
	private boolean tarFile(String filePath) {
		try{
			String srcPathName = filePath;
			String descPathName =  PreformConfig.getInstance().getConfig(PreformConfig.ZIP_PATH);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
			String dateStr = sf.format(new Date());
			descPathName = descPathName + File.separator + dateStr + ".zip";
			File file = new File(descPathName);
			if(!file.exists()){
				file.mkdirs();
			}
			ZipUtils.compress(srcPathName, descPathName);
		}catch(Exception e){
			log.error(e);
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		try {
			doTask();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	public static boolean isBusy() {
		return isBusy;
	}
}
