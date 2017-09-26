package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.dotoyo.dsform.model.MessageBo;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.util.StringsUtils;

import net.sf.json.JSONObject;

public class PreformUtils {
	public static String getVirtualPath(Map<String, String> map) throws Exception{
		String formType = map.get("formType");
		Map<String,String> param = new HashMap<String, String>(); 
		if(StringsUtils.isEmpty(formType)){
			param.put("value","formType");
			throw new FrameException("3000000000000052",param);
		}
		StringBuilder sb = new StringBuilder();
		//系统表单
		if("sys".equals(formType)){
			String orgType = map.get("orgType");
			String rootId = map.get("rootId");
			
			checkNull(orgType,"orgType");
			checkNull(rootId,"rootId");
			
			
			//   /sys/单位类型(yz)/目录id/文件模版id
			sb.append(File.separator).append("sys").append(File.separator)
				.append(orgType).append(File.separator).append(rootId).append(File.separator);
		}else if("cusEnt".equals(formType)){
			String cityId = map.get("cityId");
			String entId = map.get("entId");
			
			checkNull(cityId,"cityId");
			checkNull(entId,"entId");
			
			
			// /custom/城市id/企业id/ent/文件模版id
			sb.append(File.separator).append("custom").append(File.separator).append(cityId)
				.append(File.separator).append(entId).append(File.separator).append("ent").append(File.separator);
		}else if("cusOrg".equals(formType)){
			String cityId = map.get("cityId");
			String entId = map.get("entId");
			String orgId = map.get("orgId");
			
			checkNull(cityId,"cityId");
			checkNull(entId,"entId");
			checkNull(orgId,"orgId");
			
			// /custom/城市id/企业id/机构id/文件模版id
			sb.append(File.separator).append("custom").append(File.separator).append(cityId)
				.append(File.separator).append(entId).append(File.separator).append(orgId).append(File.separator);
		}else{
			param.put("value","formType");
			throw new FrameException("3000000000000052",param);
		}
		return sb.toString();
	}
	
	//设置模版目录
	public static void setModelDirection(TfPreformModel model, Map<String, String> map) throws Exception {
		String formType = map.get("formType");
		Map<String,String> param = new HashMap<String, String>(); 
		if(StringsUtils.isEmpty(formType)){
			param.put("value","formType");
			throw new FrameException("",param);
		}
		String virtualPath = "";
		StringBuilder sb = new StringBuilder();
		//系统表单
		if("sys".equals(formType)){
			String orgType = map.get("orgType");
			String rootId = map.get("rootId");
			
			checkNull(orgType,"orgType");
			checkNull(rootId,"rootId");
			
			model.setOrgType(orgType);
			model.setRootId(rootId);
			
			//   /sys/单位类型(yz)/目录id/文件模版id
			sb.append(File.separator).append("sys").append(File.separator)
				.append(orgType).append(File.separator).append(rootId).append(File.separator);
		}else if("cusEnt".equals(formType)){
			String cityId = map.get("cityId");
			String entId = map.get("entId");
			
			checkNull(cityId,"cityId");
			checkNull(entId,"entId");
			
			model.setCityId(cityId);
			model.setEntId(entId);
			
			// /custom/城市id/企业id/ent/文件模版id
			sb.append(File.separator).append("custom").append(File.separator).append(cityId)
				.append(File.separator).append(entId).append(File.separator).append("ent").append(File.separator);
		}else if("cusOrg".equals(formType)){
			String cityId = map.get("cityId");
			String entId = map.get("entId");
			String orgId = map.get("orgId");
			
			checkNull(cityId,"cityId");
			checkNull(entId,"entId");
			checkNull(orgId,"orgId");
			
			model.setCityId(cityId);
			model.setEntId(entId);
			model.setOrgId(orgId);
			
			// /custom/城市id/企业id/机构id/文件模版id
			sb.append(File.separator).append("custom").append(File.separator).append(cityId)
				.append(File.separator).append(entId).append(File.separator).append(orgId).append(File.separator);
		}else{
			param.put("value","formType");
			throw new FrameException("",param); 
		}
		virtualPath = sb.toString();
		model.setVirtualPath(virtualPath);//虚拟路径
		model.setFormType(formType);//表单类型
	}
	
	private static void checkNull(String value,String name) throws Exception{
		 if(StringsUtils.isEmpty(value)){
			 Map<String,String> param = new HashMap<String, String>(); 
			 param.put("value",name);
			 throw new FrameException("3000000000000052",param);
		}
	}
	
	public static Map<String, String> putRequestParam(String formType, String orgType, String rootId, String cityId,
			String entId, String orgId){
		Map<String,String> map = new HashMap<String, String>();
		if(StringUtils.isEmpty(formType)){
			map.put("formType", formType);
		}
		if(StringUtils.isEmpty(orgType)){
			map.put("orgType", orgType);
		}
		if(StringUtils.isEmpty(rootId)){
			map.put("rootId", rootId);
		}
		if(StringUtils.isEmpty(cityId)){
			map.put("cityId", cityId);
		}
		if(StringUtils.isEmpty(entId)){
			map.put("entId", entId);
		}
		if(StringUtils.isEmpty(orgId)){
			map.put("orgId", orgId);
		}
		
		return map;
	}
	
	/*
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 */
	private static String indexToColumn(int index) {
        if(index <= 0){                
        	try{                     
        		throw new Exception("Invalid parameter");                 
        	}catch (Exception e) {                         
        		e.printStackTrace();                
        	}         
        }         
        index--;         
        String column = "";         
        do{                
        	if(column.length() > 0) {
                        index--;
            }
        	column = ((char) (index % 26 + (int) 'A')) + column;
            index = (int) ((index - index % 26) / 26);
        }while(index>0);
        return column;
	}
	
	/*
	 * 计算合并列的宽度度(单位为px)
	 */
	public static double getTdSpanWidth(Sheet sheet, int startCol, int endCol) {
		double tdwidth = 0;
		for (int i = startCol; i <= endCol; i++) {
			double tempwidth = sheet.getColumnWidthInPixels(i);//获得px像素
			tdwidth = tdwidth + tempwidth;

		}
		return tdwidth;
	}
	
	/*
	 * 计算合并列的高度
	 */
	public static int getTdSpanHeight(Sheet sheet, int startRow, int endRow) {
		int tdHeight = 0;
		for (int i = startRow; i <= endRow; i++) {
			int tempHeight = sheet.getRow(i).getHeight() / 32;
			tdHeight = tdHeight + tempHeight;
		}
		return tdHeight;
	}
	
	/*
	 * 计算合并列的高度
	 */
	public static String getTdSpanHeight(ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) {
		int rowspan = 0;
		if(!"".equals(td.getRowspan())){
			rowspan = Integer.parseInt(td.getRowspan());
		}
		//计算合并单元格的高度
		int height = Integer.parseInt(tr.getHeight().substring(0, tr.getHeight().indexOf("px")));
		if(rowspan > 1){
			int thisIndex = tr.getRowNum();
			for(int i=1; i < rowspan; i++){
				ExcelTableTrBo trBo = table.getTrMap().get(thisIndex + i);
				if(trBo != null){
					height+=Integer.parseInt(trBo.getHeight().substring(0, trBo.getHeight().indexOf("px")));
				}
			}
		}
		return height + "px";
	}
	
	/**
	 * 根据文件的路径创建Workbook对象
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws Exception
	 */
	public static Workbook getExcelWorkBook(String filePath) throws Exception {
		InputStream ins = null;
		Workbook book = null;
		try {
			ins = new FileInputStream(new File(filePath));
			book = WorkbookFactory.create(ins);
		}catch(IllegalArgumentException e){
			System.out.println(e.getMessage());
			throw new FrameException("3000000000000038",null);
		}finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (Exception e) {
				}
			}
		}
		return book;
	}
	
	
	/**
	 * 找不到文件模板错误
	 * @param httpResponse
	 * @throws Exception
	 */
	
	public static void notFoundPreformError(HttpServletResponse httpResponse,String errorInfo) throws Exception{
		toErrorPage(httpResponse,"对不起,未找到该文件模版,请到系统中重新上传.仍有疑问请联系客服!",errorInfo);
	}
	
	/**
	 * 跳转到错误页面
	 * @param httpResponse http请求
	 * @param content 显示信息
	 * @param hiddenErrorInfo 隐藏的错误信息
	 * @throws Exception
	 */
	public static void toErrorPage(HttpServletResponse httpResponse,String content,String hiddenErrorInfo) throws Exception {
		PrintWriter pw = httpResponse.getWriter();
		String prefixPath = PreformConfig.getInstance().getConfig(PreformConfig.DS_PATH);// domain
		String imgPath = String.format("%s/js/easyui/themes/icons/tip_warning28.png", prefixPath);
		String hiddenInput = String.format("<input id='errorInfo' type='hidden' value='%s'/>", hiddenErrorInfo);
		
		String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com
		String domainContent = String.format("<SCRIPT type='text/javascript'>document.domain='%s';</SCRIPT>",domain);
		try {
			pw.write(String.format("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' />%s</head>"
					+ "<body><div style=' background:#ffe; border:1px solid #fea; color:#ff9b00; height:68px; padding-left:20px; line-height:68px;'>"
					+ "<img src='%s' style='vertical-align:middle; width:28px; height:28px;'/>%s</div> %s</body></html>",
					domainContent, imgPath, content, hiddenInput));
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	/**
	 * 跨域上传成功后,回调parent的方法(请求成功后,提示用户)
	 * @param httpRequest
	 * @param response
	 * @param json
	 * @throws Exception
	 */
	public static void successResponse(HttpServletRequest httpRequest,HttpServletResponse response, JSONObject json) throws Exception {
		String crossDomain = httpRequest.getParameter("crossDomain");
		//跨域请求
		if("true".equals(crossDomain)){
			String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com
			IFrameMessage msgF = FrameFactory.getMessageFactory(null);
			MessageBo bo = new MessageBo();
			bo.setRespType("json");
			@SuppressWarnings("unchecked")
			String str = msgF.getRespMessage(bo, json);
			str = String.format("<html><head><script type='text/javascript'>document.domain='%s';</script><script>" + "window.onload = function(){parent.successCallback('%s');", domain , str )
			+ "}</script></head><body></body></html>";  
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				pw.print(str);
				pw.flush();
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}else{
			WebUtils.responseJsonSuccess(response, json);
		}
	}
	
	/**
	 * 跨域上传失败后,回调parent的方法(请求成功后,提示用户)
	 * @param httpRequest
	 * @param response
	 * @param json
	 * @throws Exception
	 */
	public static void errorResponse(HttpServletRequest httpRequest,HttpServletResponse response, Exception e) throws Exception {
		String crossDomain = httpRequest.getParameter("crossDomain");
		//跨域请求
		if("true".equals(crossDomain)){
			String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com	
			IFrameMessage msgF = FrameFactory.getMessageFactory(null);
			MessageBo bo = new MessageBo();
			bo.setRespType("json");
			String str = msgF.getRespMessage(bo, e);
			str = String.format("<html><head><script type='text/javascript'>document.domain='%s';</script><script>" + "window.onload = function(){parent.successCallback('%s');", domain , str )
			+ "}</script></head><body></body></html>";  
			PrintWriter pw = null;
			try {
				pw = response.getWriter();
				pw.print(str);
				pw.flush();
			} finally {
				if (pw != null) {
					pw.close();
				}
			}
		}else{
			WebUtils.responseJsonError(response, e);
		}
	}
	
	/**
	 * 将request中的参数转为json
	 * @param httpRequest
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String converURLParam(HttpServletRequest httpRequest) {
		Enumeration enu = httpRequest.getParameterNames();
		JSONObject jsonObject = new JSONObject(); 
		while(enu.hasMoreElements()){
			String key = (String)enu.nextElement();
			String value = httpRequest.getParameter(key);
			jsonObject.put(key, value);
		}
		return jsonObject.toString();
	}
}
