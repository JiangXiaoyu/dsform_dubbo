package com.dotoyo.dsform.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;

import com.dotoyo.dsform.dao.inter.ITfPreformDao;
import com.dotoyo.dsform.excel.ExcelPictureBo;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.inter.ISerialNumService;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.Excel2MdfHtml;
import com.dotoyo.ims.dsform.allin.Excel2Table;
import com.dotoyo.ims.dsform.allin.Excel2ViewHtml;
import com.dotoyo.ims.dsform.allin.ExcelTableBo;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IExcel2Html;
import com.dotoyo.ims.dsform.allin.IExcel2Table;
import com.dotoyo.ims.dsform.allin.IFrameAnnex;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameMongodb;
import com.dotoyo.ims.dsform.allin.IFrameSequence;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.PictureStyleBo;
import com.dotoyo.ims.dsform.allin.PreformConstant;
import com.dotoyo.ims.dsform.allin.PreformUtils;
import com.dotoyo.ims.dsform.allin.RebuildAllPreform;
import com.dotoyo.ims.dsform.allin.SnEntity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TfPreformService implements ITfPreformService {	
	
	protected static final transient IFrameLog log = new LogProxy(LogFactory
			.getLog(RebuildAllPreform.class));

	private ITfPreformDao dao;

	public ITfPreformDao getDao() {
		return dao;
	}

	public void setDao(ITfPreformDao dao) {
		this.dao = dao;
	}
	
	@Override
	public int insert(TfPreformModel record) throws Exception {
		IFrameService svf = FrameFactory.getServiceFactory(null);
		ITfPreformDao dao = (ITfPreformDao) svf.getService(ITfPreformDao.class.getName());
		return dao.insert(record);
	}
	
	@Override
	public int deleteByPrimaryKey(TfPreformModel record) throws Exception {
		return dao.deleteByPrimaryKey(record);
	}	
	
	@Override
	public List<String> selectIds4Limit(Map<String, String> map) throws Exception {
		return dao.selectIds4Limit(map);
	}
	
	@Override
	public long countUnrebuilde(Map<String, String> condition) {
		return dao.countUnrebuilde(condition);
	}
	
	@Override
	public int updateByPrimaryKey(TfPreformModel record) throws Exception {
		return dao.updateByPrimaryKey(record);
	}
	
	@Override
	public TfPreformModel selectByPrimaryKey(TfPreformModel record) throws Exception {
		return dao.selectByPrimaryKey(record);
	}
	
//	@RequestMapping("getTfPreform")
	public TfPreformModel selectByPrimaryKeyB(TfPreformModel model){	
		try {
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ITfPreformService sv = (ITfPreformService) fs
					.getService(TfPreformService.class.getName());			
			
			model = sv.selectByPrimaryKey(model);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return model;
	}
	
	public String getId(AnnexModel annexModel, Map<String, String> formValueMap){
		String id = "";
		try {
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ITfPreformService sv = (ITfPreformService) fs
					.getService(TfPreformService.class.getName());
			id = sv.savePreform4Multipage(annexModel, formValueMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return id;
	}
	
//	@RequestMapping("getSeqId")
	public String getSeqId(){
		String id = "";
		try {
			IFrameSequence sF = FrameFactory.getSequenceFactory(null);
			id = sF.getSequence();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public void uploadFrameAnnex(AnnexModel annexModel, InputStream is, Map<String, String> formValueMap){
		IFrameAnnex frameAnnex;
		try {
			frameAnnex = FrameFactory.getAnnexFactory(null);
			frameAnnex.upload(annexModel, is, formValueMap);
			frameAnnex.commit(annexModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public AnnexModel uploadFrameAnnex(AnnexModel annexModel, byte[] is, Map<String, String> formValueMap){
		try {
			IFrameAnnex frameAnnex = FrameFactory.getAnnexFactory(null);
			annexModel = frameAnnex.upload(annexModel, is, formValueMap);
			frameAnnex.commit(annexModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return annexModel;
	}
	
	
	/*
	 * 保存单页模版
	 */	
	public String savePreform(AnnexModel bo, Map<String, String> map) throws Exception {
		String filePath = bo.getPath() + File.separator + bo.getId();
		// 如果Excel文件版本是2007,则抛出异常
		boolean is2007 = isExcel2007(filePath);
		if(false){
			throw new FrameException("3000000000000041",null);
		}
		
		TfPreformModel model = new TfPreformModel();
		model.setMethod("new");//新表单
		IFrameSequence fse = FrameFactory.getSequenceFactory(null);
		model.setId(fse.getSequence());//设置id
		model.setStatus(PreformConstant.PREFORM_NORMAL);//设置状态(1有效0无效)
		model.setThisType("fixed");//fixed固定类型 auto(自适应---已废弃)
		model.setFile(bo.getId());//excel文件id
		model.setCreateTime(new Timestamp(System.currentTimeMillis()));//创建时间
		PreformUtils.setModelDirection(model,map);//设置目录

		ExcelTableBo table = setPreformId4Normal(bo, model, filePath);
		
		String note = map.get("note");
		model.setNote(note != null ? note : "normal");//上传的方式   batch（批量上传） normal(单个上传) multipage(多页上传)
		String name = map.get("name");
		model.setName(name != null ? name : "");//表单名称
		String topMargin = map.get("topMargin");//上留白,默认1.0cm
		if(!StringsUtils.isEmpty(topMargin) && StringsUtils.isNumber(topMargin) ){
			model.setTopMargin(topMargin + "cm");
		}
		String leftMargin = map.get("leftMargin");//左留白,默认2.0cm
		if(!StringsUtils.isEmpty(leftMargin) && StringsUtils.isNumber(leftMargin) ){
			model.setLeftMargin(leftMargin + "cm");
		}
		List<String> snInputList = table.getSnInputList();
		if(snInputList.size() != 0){
			model.setSnInputId(StringUtils.join(snInputList, ","));//设置编号控件id
		}
		model.setSnType(table.getSnType());//设置编号类型
		model.setSnStrFormat(table.getSnStrFormat());//设置格式化编号
		model.setTotalOfSumTitle(table.getTotalOfSumTitle());//sumTitle总数(分部分项汇总)
		model.setTotalOfSumSubSec(table.getTotalOfSumSubSec());//sumSubsec总数(分部分项汇总)
		model.setItemNo(table.getItemNo());//年份单位工程分部子分部分项
		this.insert(model);
		//返回模版id
		return model.getId();
	}
	
	
	private ExcelTableBo setPreformId4Normal(AnnexModel bo,
			TfPreformModel model,String filePath)
			throws Exception, UnsupportedEncodingException, IOException {
		//html的param参数
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "html");
		htmlParMap.put("excelId", bo.getId());//创建文件模版目录使用
		htmlParMap.put("virtualPath", model.getVirtualPath());//创建文件模版目录使用

		//mdfHtml的param参数
		Map<String, String> mdfHtmlParMap = new HashMap<String, String>();
		mdfHtmlParMap.put("formcode", "TfPreform");
		mdfHtmlParMap.put("curcode", "mdfHtml");
		mdfHtmlParMap.put("excelId", bo.getId());//创建文件模版目录使用
		mdfHtmlParMap.put("virtualPath", model.getVirtualPath());//创建文件模版目录使用
		
		IExcel2Html dmfHtml =  new Excel2MdfHtml();
		IExcel2Table eu = new Excel2Table();
		//解析Excel的各个组件
		ExcelTableBo table = eu.toTable(filePath, 0);
		
		//1.生成可编辑的文件模版（可编辑）
		//	设置生成的html类型（固定类型）
		table.setThisType("fixed");
		AnnexModel mdfHtmlAnnex = uploadHtmlAnnex(mdfHtmlParMap,
				dmfHtml, table);
		model.setMdfHtml(mdfHtmlAnnex.getId());
		//设置打印方向
		model.setPrintDirection(table.getPrintDirc());
		
		//可编辑与可预览的文件模版共用图片
		List<PictureStyleBo> picList = table.getPicStyleList();
		IExcel2Html html = new Excel2ViewHtml(picList);//2003
		
		//2.生成固定类型的可预览的文件模版  （固定可预览）
		//	设置生成的html类型（固定类型）
		table.setThisType("fixed");
		AnnexModel viewHtmlAnnex = uploadHtmlAnnex(htmlParMap, html,
				table);
		model.setHtml(viewHtmlAnnex.getId());
		
		//3.生成自适应类型的可预览的文件模版（自适应可预览）
		//	设置生成的html类型（固定类型）
		table.setThisType("auto");
		AnnexModel viewHtmlAnnex4Auto = uploadHtmlAnnex(htmlParMap, html,
				table);
		model.setAutoModelId(viewHtmlAnnex4Auto.getId());
		
		//4.生成手机端可预览的文件模版（手机端可预览）
		//	设置生成的html类型（固定类型）
		table.setThisType("mobile");
		AnnexModel viewHtmlAnnex4Mobile = uploadHtmlAnnex(htmlParMap, html,
				table);
		model.setMobileModelId(viewHtmlAnnex4Mobile.getId());
		
		//5.生成范例的文件模板
		//  设置生成的html类型(范例类型)
		AnnexModel viewHtmlAnnex4Eam = uploadHtmlAnnex4Example(htmlParMap, html,
				filePath,"example");
		model.setExampleId(viewHtmlAnnex4Eam != null ? viewHtmlAnnex4Eam.getId() : "");
		
		//6.生成填写说明的文件模板
		//  设置生成的html类型(填写说明类型)
		AnnexModel viewHtmlAnnex4FillInstr = uploadHtmlAnnex4Example(htmlParMap, html,
				filePath,"fillInstr");
		model.setFillInstrId(viewHtmlAnnex4FillInstr != null ? viewHtmlAnnex4FillInstr.getId() : "");
		return table;
	}
	
	/**
	 * 上传单页文件模版
	 * @param htmlParMap
	 * @param dmfHtml
	 * @param table
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private AnnexModel uploadHtmlAnnex(Map<String, String> htmlParMap, IExcel2Html dmfHtml,
			ExcelTableBo table) throws Exception{
		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		clearTableData(table);//param必须重置为空
		ByteArrayInputStream mdfByterInputStream = null;
		//生成html字符串
		String mdfHtmlStr = dmfHtml.toHtml(table);
		//上传可编辑器的文件模版
		AnnexModel htmlAnnex = new AnnexModel();
		try {
			mdfByterInputStream = new ByteArrayInputStream(mdfHtmlStr.getBytes("utf-8"));
			iAnnex.upload(htmlAnnex, mdfByterInputStream, htmlParMap);
			iAnnex.commit(htmlAnnex);
		} finally {
			if (mdfByterInputStream != null) {
				mdfByterInputStream.close();
			}
		}
		return htmlAnnex;
	}
	

	//预览时不必重新解析table,直接使用编辑的table,但param和pic的属性必须清空.
	private void clearTableData(ExcelTableBo table){
		table.getParam().clear();
		List<PictureStyleBo> picList = table.getPicStyleList();
		for(PictureStyleBo pic : picList){
			pic.setViewStatus("0");
			pic.setMdfStatus("0");
		}
		table.setPicStyleList(picList);
	}
	
	private boolean isExcel2007(String filePath) {
		try {
			new XSSFWorkbook(new FileInputStream(filePath));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 上传范例或填写说明并返回上传文件对象
	 * @param htmlParMap 上传的预览表单参数
	 * @param dmfHtml
	 * @param filePath excel路径位置
	 * @param type 范例或填写说明
	 * @return 范例或填写说明的id
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private AnnexModel uploadHtmlAnnex4Example(Map<String, String> htmlParMap, IExcel2Html dmfHtml,
			String filePath,String type) throws Exception{
		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		Workbook workbook = PreformUtils.getExcelWorkBook(filePath);
		int sheetNum = workbook.getNumberOfSheets();
		int sheetIndex = -1;
		for(int i = 0; i < sheetNum; i++){
			String sheetName = workbook.getSheetName(i);
			if(type.equalsIgnoreCase(sheetName)){
				sheetIndex = i;
				break;
			}
		}
		if(sheetIndex == -1){
			return null;
		}
		
		IExcel2Table eu = new Excel2Table();
		ExcelTableBo table = eu.toTable(filePath, sheetIndex);
		table.setThisType(type);
		//生成html字符串
		String mdfHtmlStr = dmfHtml.toHtml(table);
		//上传可编辑器的文件模版
		AnnexModel htmlAnnex = new AnnexModel();
		ByteArrayInputStream mdfByterInputStream = null;
		try {
			mdfByterInputStream = new ByteArrayInputStream(mdfHtmlStr.getBytes("utf-8"));
			iAnnex.upload(htmlAnnex, mdfByterInputStream, htmlParMap);
			iAnnex.commit(htmlAnnex);
		} finally {
			if (mdfByterInputStream != null) {
				mdfByterInputStream.close();
			}
		}
		return htmlAnnex;
	}
	
	/**
	 * 保存多页模板
	 */
	@Override
	public String savePreform4Multipage(AnnexModel bo, Map<String, String> map) throws Exception {
		IFrameSequence fse = FrameFactory.getSequenceFactory(null);
		TfPreformModel model = new TfPreformModel();
		model.setMethod("new");//新表单
		model.setId(fse.getSequence());//id
		model.setStatus(PreformConstant.PREFORM_NORMAL);//状态
		model.setNote("multipage");//多页
		model.setThisType("fixed");//固定类型
		model.setFile(bo.getId());//excel文件id
		model.setCreateTime(new Timestamp(System.currentTimeMillis()));//创建时间
		String fileName = map.get("fileName");
		model.setName(fileName != null ? fileName : "" );//表单名称
		PreformUtils.setModelDirection(model,map);	//设置目录
		String topMargin = map.get("topMargin");//上留白,默认1cm
		if(!StringsUtils.isEmpty(topMargin) && StringsUtils.isNumber(topMargin) ){
			model.setTopMargin(topMargin + "cm");
		}
		String leftMargin = map.get("leftMargin");//左留白,默认2cm
		if(!StringsUtils.isEmpty(leftMargin) && StringsUtils.isNumber(leftMargin) ){
			model.setLeftMargin(leftMargin + "cm");
		}
		
		List<String> printDireList = new ArrayList<String>();//打印方向list
		//1.上传固定类型可编辑的文件模版（多页模版）
		//	图片解析出来重用
		List<PictureStyleBo> allPicList = new ArrayList<PictureStyleBo>();
		//	table解析出来重用
		List<ExcelTableBo> tableList = new ArrayList<ExcelTableBo>();
		map.put("thisType", "fixed");
		AnnexModel mdfHtmlAnnex  = uploadMdfHtml4Mul(bo.getId(),model,map,tableList,allPicList,printDireList);

		//2.上传固定类型可预览的文件模版（多页模版）
		map.put("thisType", "fixed");
		AnnexModel viewhtmlAnnex = uploadViewHtml4Mul(bo.getId(),model,map, allPicList,
				tableList);
		
		//3.上传自适应类型可预览的文件模版（多页模版）
		map.put("thisType", "auto");
		AnnexModel viewhtmlAnnex4Auto = uploadViewHtml4Mul(bo.getId(),model,map, allPicList,
				tableList);
		
		//4.上传手机版可预览的文件模版（多页模版）
		map.put("thisType", "mobile");
		AnnexModel viewhtmlAnnex4Mobile = uploadViewHtml4Mul(bo.getId(),model,map, allPicList,
				tableList);
		
		

		model.setMdfHtml(mdfHtmlAnnex.getId());//固定类型可编辑的文件模版
		model.setHtml(viewhtmlAnnex.getId());//固定类型可预览的文件模版
		model.setAutoModelId(viewhtmlAnnex4Auto.getId());//自适应类型的可预览的文件模版
		model.setMobileModelId(viewhtmlAnnex4Mobile.getId());//手机版的可预览的文件模版


		StringBuilder sb = new StringBuilder("");
		for(String pd : printDireList){
			sb.append(pd);
			sb.append(",");
		}
		if(sb.length() !=0){//打印方向
			model.setPrintDirection(sb.substring(0, sb.length()-1));
		}
		
		if(tableList !=null && tableList.size()!=0){
			ExcelTableBo table = tableList.get(0);
			model.setSnType(table.getSnType());//设置编号类型
			model.setSnStrFormat(table.getSnStrFormat());//设置格式化编号
			List<String> snInputList = table.getSnInputList();
			if(snInputList.size() != 0){
				model.setSnInputId(StringUtils.join(snInputList, ","));
			}
		}
		this.insert(model);
		return model.getId();
	}
	
	/**
	 * 上传固定类型可编辑的多页文件模版
	 * @param excelId excel的附件id
	 * @param model 表单实体对象
	 * @param map   参数集合
	 * @param tableList  所有的tablebo对象
	 * @param allPicList 所有图片
	 * @param printDirection 打印方向
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	private AnnexModel uploadMdfHtml4Mul(String excelId,TfPreformModel model,Map<String, String> map,
			List<ExcelTableBo> tableList,List<PictureStyleBo> allPicList,List<String> printDirection) throws Exception {
		int startIdValue = 0;
		AnnexModel mdfHtmlAnnex = new AnnexModel();
		ByteArrayInputStream mdfByterInputStream = null;

		String filePath = map.get("path");
		File file = new File(filePath);

		IExcel2Html dmfHtml = new Excel2MdfHtml();
		IExcel2Table eu = new Excel2Table();

		StringBuilder mdfTotalStr = new StringBuilder("");
		boolean hasEditor = false;
		
		InputStream ins = new FileInputStream(file);
		Workbook book = WorkbookFactory.create(ins);
		int sheetNum = book.getNumberOfSheets();
		
		List<ExcelPictureBo> picList =null;
		List<PictureStyleBo> picStyList = null;
		for (int index = 0; index < sheetNum; index++) {
			String sheetName = book.getSheetName(index);
			if("example".equals(sheetName) || "fillInstr".equals(sheetName)){
				continue;//范例或填写说明 不是正文
			}
			ExcelTableBo table = eu.toTable(filePath,index);
			tableList.add(table);
			printDirection.add(table.getPrintDirc());
			
			String thisType = map.get("thisType");
			table.setThisType(thisType !=null ? thisType : "");
			
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
			//第一次循环就可以取出所有图片,那么  allPicList.size() 不空时,可以放弃
			if (table.getPicStyleList() != null && table.getPicStyleList().size() != 0 && allPicList.size() ==0) {
				allPicList.addAll(table.getPicStyleList());
			}
			startIdValue += table.getParam().size();//控件id的值
			
			if(table.isHasEditor()){
				hasEditor = true;
			}
		}

		// 查找mdfHtml的param参数
		Map<String, String> mdfHtmlParMap = new HashMap<String, String>();
		mdfHtmlParMap.put("formcode", "TfPreform");
		mdfHtmlParMap.put("curcode", "mdfHtml");
		mdfHtmlParMap.put("excelId", excelId);//创建文件模版目录使用
		mdfHtmlParMap.put("virtualPath", model.getVirtualPath());//创建文件模版目录使用

		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		try {
			mdfByterInputStream = new ByteArrayInputStream(mdfTotalStr.toString().getBytes("utf-8"));
			iAnnex.upload(mdfHtmlAnnex, mdfByterInputStream, mdfHtmlParMap);
			iAnnex.commit(mdfHtmlAnnex);
		} finally {
			if (mdfByterInputStream != null) {
				mdfByterInputStream.close();
			}
		}
		return mdfHtmlAnnex;
	}
	
	/**
	 * 上传可预览的多页文件模版（固定类型 或 自适应 或手机端）
	 * @param excelId excel附件id
	 * @param model 表单实体对象
	 * @param map 参数集合
	 * @param allPicList 所有的图片
	 * @param tableList 所有的tablebo
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private AnnexModel uploadViewHtml4Mul(String excelId,TfPreformModel model,Map<String, String> map,
			List<PictureStyleBo> allPicList,List<ExcelTableBo> tableList) throws
			UnsupportedEncodingException, IOException, Exception {
		Map<String, String> htmlParMap = new HashMap<String, String>();
		htmlParMap.put("formcode", "TfPreform");
		htmlParMap.put("curcode", "html");
		htmlParMap.put("excelId", excelId);//创建文件模版目录使用
		htmlParMap.put("virtualPath", model.getVirtualPath());//创建文件模版目录使用
		
		int startIdValue = 0;//控件id的起始值
		IExcel2Html html = new Excel2ViewHtml(allPicList);// 2003
		StringBuilder viewTotalStr = new StringBuilder("");
		
		List<ExcelPictureBo> picList =null;
		List<PictureStyleBo> picStyList = null;
		for (int index = 0; index < tableList.size(); index++) {
			ExcelTableBo table = tableList.get(index);	
			String thisType = map.get("thisType");
			table.setThisType(thisType != null ? thisType : "");
			
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
					//viewTotalStr.insert(startIndex, "<div style='page-break-after:always'>&nbsp;</div> ");
					viewTotalStr.insert(startIndex, "<!--mulPreform-->");
				}
			}
			startIdValue += table.getParam().size();//控件id的起始值
		}
	
		AnnexModel viewhtmlAnnex = new AnnexModel();
		ByteArrayInputStream viewByterInputStream = null;
		IFrameAnnex iAnnex = FrameFactory.getAnnexFactory(null);
		try {
			viewByterInputStream = new ByteArrayInputStream(viewTotalStr.toString().getBytes("utf-8"));
			iAnnex.upload(viewhtmlAnnex, viewByterInputStream, htmlParMap);
			iAnnex.commit(viewhtmlAnnex);
		} finally {
			if (viewByterInputStream != null) {
				viewByterInputStream.close();
			}
		}
		return viewhtmlAnnex;
	}
	
	
	@Override
	public JSONObject getPreformData4Mongodb(JSONObject reqJson) throws Exception {
		String _id = reqJson.optString("_id");
		if (StringsUtils.isEmpty(_id) || "undefined".equals(_id)) {
			return new JSONObject();
		}
		IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
		BasicDBObject dbId = new BasicDBObject();
		dbId.put("_id", new ObjectId(_id));
//		DBObject result = mongodb.findById("preformData", dbId);
		DBObject result = new BasicDBObject();//TODO
		return JSONObject.fromObject(result);
	}
	
	/*
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JSONObject savePreformData4Mongodb(JSONObject reqJson, JSONObject jsonObject) throws Exception {
		JSONObject resultJson = new JSONObject();
		IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
		String _id = reqJson.optString("_id");
		String orgId = !reqJson.optString("orgId").equals("") ? reqJson.optString("orgId") : reqJson.optString("companyId") ;
		
		if (StringsUtils.isEmpty(orgId)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("param", "orgId");
			throw new FrameException("3000000000000035", map);
		}
		if ("".equals(_id)) {
			reqJson.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			// 存储orgId
			if (!StringsUtils.isEmpty(orgId)) {
				Iterator ite = reqJson.keys();
				JSONArray value = new JSONArray();
				Pattern pattern = Pattern.compile("^A\\d*$");
				while (ite.hasNext()) {
					String key = ite.next().toString();
					if (pattern.matcher(key).matches()) {
						value.add(key);
					}
				}
				reqJson.put(orgId, value);// 当前
				reqJson.put("firstOrder", orgId);// 文件发起的orgId
				reqJson.put("endOrder", orgId);// 文件最后处理的orgId
				JSONArray orderList = new JSONArray();
				orderList.add(orgId);
				reqJson.put("orderList", orderList);// 处理的机构id列表
			}
			DBObject dbObject = (DBObject) JSON.parse(reqJson.toString());
			dbObject.put("status", PreformConstant.PREFORM_FILE_NO_ARCHIVE);// 1表示文件未归档
			
			TfPreformModel preformModel =  getPreformById(reqJson.optString("id"));
			//获得保存编号和流水号
			SnEntity snEnt = getSaveSn(reqJson, preformModel);
			//重新设置流水号控件的值
			resetSnInput4Mongodb(reqJson, dbObject, preformModel);
			dbObject.put("sn", snEnt != null ? snEnt.getSn() : "");
			dbObject.put("no", snEnt != null ? snEnt.getNo() : "");
			//项目id为空，则为空
			if(dbObject.get("projectId")==null){
				dbObject.put("projectId", "");
			}
			// 0表示文件已归档
//			mongodb.insert("preformData", dbObject);//TODO
			_id = dbObject.get("_id").toString();
			reqJson.put("_id", _id);
			//返回特殊数据
			returnSpecData(resultJson, dbObject, snEnt);
			//updateIndex4Lucene(dbObject,_id,orgId);
			reqJson.putAll(resultJson);
			return reqJson;
		} else {
			reqJson.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			DBObject dbObject = (DBObject) JSON.parse(reqJson.toString());
			BasicDBObject dbId = new BasicDBObject();
			dbId.put("_id", new ObjectId(_id));
//			DBObject result = mongodb.findById("preformData", dbId);
			DBObject result = new BasicDBObject();//TODO

			// 查询文件是否已经归档
			String status = result.get("status").toString();
			if (status != null && PreformConstant.PREFORM_FILE_ARCHIVE.equals(status)) {// 1表示文件未归档
				// 0表示文件已归档
				throw new FrameException("3000000000000037", null);// 提示文件已经归档
			}
			/*
			 * 遍历已经保存的控件的输入值，如果已经保存，那么文件流转时忽略 例如： 发起文件时A1控件已经有值，那么文件流转再覆盖A1控件的值时，必选判断orgId是否一致。
			 */
			Pattern pattern = Pattern.compile("^A\\d*$");
			List<String> removeList = new ArrayList<String>();
			
			List<String> orderList = (List<String>) result.get("orderList");//机构列表
			orderList.remove(orgId);//不包括当前机构的机构列表
			for (String dkey : dbObject.keySet()) {
				if (pattern.matcher(dkey).matches() && result.get(dkey) != null) {
					
					for(String oId : orderList){
						List<String> valueList =(List<String>) result.get(oId);
						if(valueList.contains(dkey)){
							removeList.add(dkey);
							break;
						}
					}
					/*// 发现有覆盖值的情况，判断当前ordId是否有权利写过此控件的值
					if (result.get(orgId) != null) {
						List<String> value = (List<String>) result.get(orgId);
						String endOrder = result.get("endOrder").toString();
						// value是当前机构所拥有的所有控件
						//if (value.contains(dkey) && endOrder.equals(orgId)) {
						if (!value.contains(dkey) && endOrder.equals(orgId)) {
							
						}
					} else {
						removeList.add(dkey);
					}*/
				}
			}
			for (String rKey : removeList) {
				dbObject.removeField(rKey);
			}

			// 存储orgId
			Set<String> keySet = dbObject.keySet();
			JSONArray jValue = null;// 机构id所属的控件集合
			if (result.get(orgId) == null) {
				jValue = new JSONArray();
			} else {
				List<String> valueList = (List<String>) result.get(orgId);
				jValue = JSONArray.fromObject(valueList);
			}
			for (String keyStr : keySet) {
				if (pattern.matcher(keyStr).matches()) {
					jValue.add(keyStr);
				}
			}
			dbObject.put(orgId, jValue);// 设置orgId的所属控件
			dbObject.put("endOrder", orgId);// 文件最后处理的orgId

			JSONArray orderArray = null;
			if (result.get("orderList") == null) {
				orderArray = new JSONArray();
			} else {
				List<String> valueList = (List<String>) result.get("orderList");
				orderArray = JSONArray.fromObject(valueList);
			}
			orderArray.add(orgId);
			dbObject.put("orderList", orderArray);// //处理的机构id列表
			
			
			//添加样式
			DBObject oldStyle = new BasicDBObject();//旧样式
			if (result.get("intSty")!= null) {
				oldStyle = (DBObject) result.get("intSty");
			} 
			
			DBObject newStyle = (DBObject)dbObject.get("intSty");//新样式
			DBObject allStyle = new BasicDBObject();
			
			if(oldStyle != null && newStyle !=null){
				Set<String> keys = oldStyle.keySet();
				Set<String> nKeys = newStyle.keySet();
				for(String key : keys){
					DBObject oDb = (DBObject)oldStyle.get(key);
					allStyle.put(key, oDb);
				}
				
				for(String nKey : nKeys ){
					DBObject db = (DBObject)newStyle.get(nKey);
					if(allStyle.get(nKey)!=null){
						DBObject d = (DBObject)allStyle.get(nKey);//旧的
						
						Set<String> dkeys = d.keySet();
						for(String dkey : dkeys){
							if(!db.containsField(dkey)){
								db.put(dkey, d.get(dkey));//新的
							}
						}
					}
					allStyle.put(nKey, db);
				}
				dbObject.put("intSty", allStyle);
			}else if(newStyle !=null){
				dbObject.put("intSty", newStyle);
			}
			
			dbObject.removeField("_id");// 删除_id才能更新
//			mongodb.updateById("preformData", dbObject, _id);//TODO
			//updateIndex4Lucene(dbObject,_id,orgId);
			resultJson.put("_id", _id);
			//更新时预警
			remind4Dspl110(result,dbObject,resultJson);
			reqJson.putAll(resultJson);
			return reqJson;
		}
	}
	
	private TfPreformModel getPreformById(String id) throws Exception {
		TfPreformModel preform = new TfPreformModel();
		preform.setId(id);
		preform = this.selectByPrimaryKey(preform);
		return preform;
	}
	
	private SnEntity getSaveSn(JSONObject reqJson,TfPreformModel preform) throws Exception {
		String companyId = reqJson.optString("companyId");//没有公司id 则不保存编号
		String draft = reqJson.optString("draft");//draft = 1 为草稿
		if("".equals(companyId) || "1".equals(draft)){ //当公司为空  或 为草稿箱时，忽略文件编号
			return null;
		}
		String snType = preform.getSnType();
		//mulsn secsn编号由用户自定义
		if(StringUtils.isEmpty(snType) ){
			return null;
		}
		if("mulsn".equals(snType) || "secsn".equals(snType) ){
			SnEntity se = new SnEntity();
			String saveSn = reqJson.optString("saveSn");
			se.setSn(saveSn);
			return se;
		}
		
		IFrameService fs = FrameFactory.getServiceFactory(null);
		ISerialNumService sv = (ISerialNumService) fs.getService(ISerialNumService.class.getName());
		SnEntity snEnt = sv.lockSaveSn(reqJson);
		String saveSn = snEnt.getSn();//saveSn文件编号
		reqJson.put("saveSn", saveSn);//resetSnInput4Mongodb时需要用到
		String snFormat =  preform.getSnStrFormat();
		String snInputId =  preform.getSnInputId();
		if(!StringsUtils.isEmpty(snFormat)){
			Object[] arrayStr = saveSn.split("-");
			String[] snId = snInputId.split(",");
			if("cmsn".equals(snType)){
				//只有在cmsn下才会走这里
				if(arrayStr.length != snId.length){ 
					if(snId.length == 1){
						arrayStr[0] = arrayStr[0].toString() + arrayStr[1].toString() + arrayStr[2].toString();
					}else{
						arrayStr[0] = arrayStr[0].toString() + arrayStr[1].toString();
						arrayStr[1] = arrayStr[2].toString();
					}
				}
			}else if("|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){
				arrayStr = new String[]{saveSn};;
			}
			saveSn =  String.format(snFormat, arrayStr);
		}
		snEnt.setSn(saveSn);
		return snEnt;
	}
	

	/*
	 * 重新设置Mongodb中流水号控件的值
	 */
	private void resetSnInput4Mongodb(JSONObject reqJson, DBObject dbObject,TfPreformModel preform) throws Exception{
		String saveSn = reqJson.optString("saveSn");
		if("".equals(saveSn)){
			return;
		}
		
		String snType = preform.getSnType();
		String snInputId = preform.getSnInputId();
		String[] ids = snInputId.split(",");
		String[] vals = saveSn.split("-");
		
		if(ids.length == 1){
			if("|sn|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){
				dbObject.put(ids[0], saveSn);
			}else if("cmsn".equals(snType)){
				dbObject.put(ids[0], vals[0] + vals[1] + vals[2] );
			}
		}
		
		if(ids.length == 2){
			if("yearsn".equals(snType)){
				dbObject.put(ids[0], vals[0]);
				dbObject.put(ids[1], vals[1]);
			}else if("cmsn".equals(snType)){
				dbObject.put(ids[0], vals[0] + vals[1]);
				dbObject.put(ids[1], vals[2]);
			}
		}
		
		if(ids.length == 3){
			if("cmsn".equals(snType)){
				dbObject.put(ids[0], vals[0]);
				dbObject.put(ids[1], vals[1]);
				dbObject.put(ids[2], vals[2]);
			}
		}
	}
	

	/**
	 * 保存时返回特殊数据
	 * @param resultJson
	 * @param dbObject
	 * @param snEnt
	 */
	private void returnSpecData(JSONObject resultJson, DBObject dbObject,
			SnEntity snEnt) {
		//预警数据
		if(dbObject.get("dspl110Data") != null){
			resultJson.put("dspl110Data", dbObject.get("dspl110Data").toString());
		}
		if(dbObject.get("title") != null){
			resultJson.put("title", dbObject.get("title").toString());
		}
		if(snEnt != null){
			//sn数据
			resultJson.put("sn", snEnt.getSn());
			//顺序号
			resultJson.put("no", snEnt.getNo());
		}
		//通知单id
		if(dbObject.get("noticeId") != null){
			resultJson.put("noticeId", dbObject.get("noticeId").toString());
		}
		//单位工程分部子分部分项部位
		
	}
	
	//更新时预警提醒
	private void remind4Dspl110(DBObject result, DBObject dbObject,
			JSONObject resultJson) {
		DBObject dspl110Arr =  result.get("dspl110Data")!=null ? (DBObject)result.get("dspl110Data") : new BasicDBObject();
		DBObject dspl110ArrNew = dbObject.get("dspl110Data")!=null ? (DBObject)dbObject.get("dspl110Data") : new BasicDBObject();
		//新旧预警不一致时,重新预警提醒
		if(!dspl110Arr.toString().equals(dspl110ArrNew.toString())){
			if(dbObject.get("dspl110Data") != null){
				resultJson.put("dspl110Data", dspl110ArrNew.toString());
			}
		}
	}
	
	public JSONObject loadPrintDirect(String id, String _id) throws Exception{		
		
		JSONObject jObj = new JSONObject();
		if(StringsUtils.isEmpty(id) || id.equals("undefined")){
			
			if(StringsUtils.isEmpty(_id) || _id.equals("undefined")){
				return jObj;
			}
			BasicDBObject dbId = new BasicDBObject();
			dbId.put("_id", new ObjectId(_id));
			IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
//			DBObject result = mongodb.findById("preformData", dbId);
			DBObject result = new BasicDBObject();//TODO
			id = result.get("id").toString();
		}
		TfPreformModel record = new TfPreformModel();
		record.setId(id);
		record = dao.selectByPrimaryKey(record);
		
		if(record != null){
			jObj.put("printDirec", record.getPrintDirection());//打印方向
			jObj.put("note", record.getNote());//表单类型
			jObj.put("topMargin", record.getTopMargin());
			jObj.put("leftMargin", record.getLeftMargin());
			jObj.put("snType", record.getSnType());//编号类型
		}
		return jObj;
	}

}
