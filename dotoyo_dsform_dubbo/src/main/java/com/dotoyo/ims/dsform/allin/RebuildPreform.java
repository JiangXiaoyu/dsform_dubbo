package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotoyo.dsform.dao.inter.IAnnexDao;
import com.dotoyo.dsform.dao.inter.IFormModelDao;
import com.dotoyo.dsform.excel.ExcelColumnBo;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.TfPreformService;


/**
 * 重新生成文件模版的html文件
 * 
 * @author wangl
 * 
 */
public class RebuildPreform implements Runnable, IThread {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(RebuildPreform.class));

	private String preformId = "";
	
	private TfPreformModel model = null;

	public RebuildPreform(String preformId) {
		this.preformId = preformId;
	}

	@Override
	public void doTask() throws Exception {
		/*String filePath = getFilePath();
		log.debug("########rebuild preform start!!");
		log.debug("########compress start!!");
		
		IFrameService fs = FrameFactory.getServiceFactory(null);
		ITfPreformService sv = (ITfPreformService) fs
				.getService(TfPreformService.class.getName());
		model = new TfPreformModel();
		model.setId(preformId);
		model = sv.selectByPrimaryKey(model);
		
		// 压缩打包，出问题后可以回滚
		//boolean flag = tarFile(filePath);
		if (!flag) {
			log.error("########compress error!!");
			log.error("########Thread stop!!");
			return;
		}

		log.debug("########compress end!!");
		// 重新生成HTML
		rebuildPreform(filePath);

		log.debug("########rebuild preform end!!");*/
	}

	private String getFilePath() throws Exception {
		// 查找html的param参数
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "html");
		IFrameService fs = FrameFactory.getServiceFactory(null);
		IFormModelDao formModelDao = (IFormModelDao) fs
				.getService(IFormModelDao.class);
		List<FormElmParModel> parList = formModelDao
				.queryFormElePar(htmlParMap);
		if (parList == null || parList.size() < 1) {
			FrameException fe = new FrameException("", null);
			throw fe;
		}
		FormElmParModel parModel = parList.get(0);
		String filePath = parModel.getThisValue();
		return filePath;
	}

	private void rebuildPreform(String filePath) throws Exception {
		IFrameService fs = FrameFactory.getServiceFactory(null);
		ITfPreformService sv = (ITfPreformService) fs.getService(TfPreformService.class.getName());
		
		IExcel2Html dmfHtml = new Excel2MdfHtml();
		Excel2Table eu = new Excel2Table();
		
		if("auto".equals( model.getThisType())){
			return;
		}
		String excelId = model.getFile();
		String excelFilePath = filePath + File.separator + model.getFile();
		
		boolean is2007 = isExcel2007(filePath + File.separator + model.getMdfHtml());
		File file = new File(excelFilePath);
		if(!file.exists()){
			return;
		}

		if(is2007){
			//2007不管
			
		}else{
			try{
				if("multipage".equals(model.getNote())){//处理多页模版
					
					HashMap<String,String> map = new HashMap<String,String>();
					//1.上传固定类型可编辑的文件模版（多页模版）
					//	图片解析出来重用
					List<PictureStyleBo> allPicList = new ArrayList<PictureStyleBo>();
					//	table解析出来重用
					List<ExcelTableBo> tableList = new ArrayList<ExcelTableBo>();
					map.put("path", excelFilePath);
					map.put("thisType", "fixed");
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
					
					//1生成固定类型的可编辑的文件模版  
					//	设置生成的html类型（固定类型）
					
					map.put("id", model.getMdfHtml());
					String mdfId = uploadHtmlAnnex( map,dmfHtml, table);
					model.setMdfHtml(mdfId);
					
					//2生成固定类型的可预览的文件模版  （固定可预览）
					//	可编辑与可预览的文件模版共用图片
					List<PictureStyleBo> picList = table.getPicStyleList();
					IExcel2Html html = new Excel2ViewHtml(picList);
					//	设置生成的html类型（固定类型）
					table.setThisType("fixed");
					map.put("id", model.getHtml());
					String hId = uploadHtmlAnnex( map, html, table);
					model.setHtml(hId);
					
					//3生成自适应类型的可预览的文件模版（自适应可预览）
					//	设置生成的html类型（固定类型）
					table.setThisType("auto");
					map.put("id", model.getAutoModelId());
					String aId =uploadHtmlAnnex( map, html, table);
					model.setAutoModelId(aId);
					
					//4生成手机端可预览的文件模版（手机端可预览）
					//	设置生成的html类型（固定类型）
					table.setThisType("mobile");
					map.put("id", model.getMobileModelId());
					String mId = uploadHtmlAnnex( map, html, table);
					model.setMobileModelId(mId);
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error(e);
			}
			}
			sv.updateByPrimaryKey(model);
			log.error("########build "+  excelId +" end!!");
	}
	
	//上传单页文件模版
	private String uploadHtmlAnnex(Map<String, String> map, IExcel2Html dmfHtml,
			ExcelTableBo table) throws Exception,
			UnsupportedEncodingException, IOException {
		//param必须重置为空
		table.setParam(new HashMap<String, ExcelColumnBo>());
		//生成html页面	
		String mdfHtmlStr = dmfHtml.toHtml(table);
		
		String id = map.get("id");
		return writeToFile(mdfHtmlStr, id);
	}

	private String writeToFile(String mdfHtmlStr, String id)
			throws Exception {
		
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "html");
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
				iAnnex.upload(htmlAnnex, mdfByterInputStream, htmlParMap);//add preform
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
				iAnnex.update(htmlAnnex, mdfByterInputStream, htmlParMap);//update preform
				iAnnex.commit(htmlAnnex);
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
			
			for (int index = 0; index < sheetNum; index++) {
				ExcelTableBo table = eu.toTable(filePath,index);
				tableList.add(table);
				
				//设置生成的html类型（固定类型）
				table.setThisType(map.get("thisType"));
				
				if(hasEditor){
					table.setHasEditor(hasEditor);//设置全局是否有编辑器
				}
				table.setStartIdValue(startIdValue);//设置控件id的初始值
				String mdfHtmlStr = dmfHtml.toHtml(table);
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
						mdfTotalStr.insert(startIndex, "<div style='page-break-after:always'>&nbsp;</div> ");
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
			if( ins != null){
				ins.close();
			}
		}

		String id = map.get("id");
		return writeToFile(mdfTotalStr.toString(), id);
	}
	
	private String uploadViewHtml4Mul(Map<String, String> map,
			List<PictureStyleBo> allPicList,
			List<ExcelTableBo> tableList) throws Exception,
			UnsupportedEncodingException, IOException {
		int startIdValue = 0;//控件id的初始值
		IExcel2Html html = new Excel2ViewHtml(allPicList);// 2003
		StringBuilder viewTotalStr = new StringBuilder("");
		
		for (ExcelTableBo table : tableList) {
			//param必须重置
			table.setParam(new HashMap<String, ExcelColumnBo>());
			
			if(map.get("thisType")!=null){
				table.setThisType(map.get("thisType"));
			}
			
			table.setStartIdValue(startIdValue);//设置控件id的初始值
			String viewHtmlStr = html.toHtml(table);
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
					viewTotalStr.insert(startIndex, "<div style='page-break-after:always'>&nbsp;</div>");
				}
			}
			startIdValue += table.getParam().size();//控件id的值
		}
	
		String id = map.get("id");
		return writeToFile(viewTotalStr.toString(), id);
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
}
