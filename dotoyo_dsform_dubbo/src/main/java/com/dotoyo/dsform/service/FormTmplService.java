package com.dotoyo.dsform.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;

import com.alibaba.dubbo.config.annotation.Service;
import com.dotoyo.dsform.dao.inter.IFormModelDao;
import com.dotoyo.dsform.interf.IFormTmplService;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.inter.IAnnexSv;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.FormElmParModel;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameMongodb;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.PreformConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import net.sf.json.JSONObject;

/**
 * 
 * @author admin
 *
 */

@Service
public class FormTmplService implements IFormTmplService {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FormTmplService.class));

	public void  testView(HttpServletRequest request){
		String _id = request.getParameter("_id");
		String parmas = request.getParameter("parmars");
	}
	
	
	
	/**
	 * 
	 * @param sign
	 * @param id	文件模板id
	 * @param _id	文件实例id
	 * @param type	类型  edit,view
	 * @param heightType
	 * @param type2
	 * @param creditSys	诚信平台需要替换domain
	 * @param orgId		机构id 
	 * @param test		本地需要去掉domain
	 * @throws FrameException
	 * @throws Exception
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	
	public static void main(String[] arg){
		FormTmplService tf = new FormTmplService();
		try {
			tf.viewPreform2("1", "1", "1", "1", "1", "1", "1", "1", "1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public File viewPreform2(String sign, String id, String _id, String type, String heightType, String type2,
			String creditSys, String orgId, String test) throws Exception{
		String errorInfo = "";
		Date start = new Date();
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITfPreformService sv = (ITfPreformService) svF
				.getService(TfPreformService.class.getName());
//		TfPreformService ssv = new TfPreformService();
//		ITfPreformDao dao = new ITfPreformDao();
//		ssv.setDao(dao);
		TfPreformModel preform = new TfPreformModel();
		// 通过实例id找到文件模版id
		DBObject result = null;
		if (!StringsUtils.isEmpty(_id) && !"undefined".equals(_id)) {
			result = findMongoDataById(_id);
			if(result == null){
//				PreformUtils.notFoundPreformError(httpResponse,"Mongod数据为空!");//找不到Mongod数据
				throw new Exception("Mongod数据为空!");
			}
			id = result.get("id").toString();
		}
		if (StringsUtils.isEmpty(id)) {
//			PreformUtils.notFoundPreformError(httpResponse,"文件模板id为空!");//找不到Mongod数据里的文件模板id
			throw new Exception("文件模板id为空!");
		}
		preform.setId(id);
		
//		 SqlSession sqlSession = SqlSessionHelper.getSessionFactory().openSession();            
//         ITfPreformDao itfPreformDao = sqlSession.getMapper(ITfPreformDao.class);	
//         preform = itfPreformDao.selectByPrimaryKey(preform);
         
         preform = sv.selectByPrimaryKey(preform);

		if (preform == null) {
//			PreformUtils.notFoundPreformError(httpResponse,"Mysql数据为空!");//找不到文件模板
			throw new Exception("Mysql数据为空!");
		}

		IAnnexSv annexSv = (IAnnexSv) svF.getService(IAnnexSv.class.getName());
		AnnexModel bo = null;
		if ("edit".equals(type)) {
			bo = annexSv.getAnnexById(preform.getMdfHtml());
		} else {
			if ("auto".equals(heightType)) {
				//auto 自适应高度预览
				String autoModelId = preform.getAutoModelId();
				bo = annexSv.getAnnexById(autoModelId);
			}else if("mobile".equals(type2)){ 
				//mobile 手机端预览
				String mobileModelId = preform.getMobileModelId();
				bo = annexSv.getAnnexById(mobileModelId);
			}else if("example".equals(type2)){
				//范例
				String mobileModelId = preform.getExampleId();
				bo = annexSv.getAnnexById(mobileModelId);
			}else if("fillInstr".equals(type2)){
				//填写说明
				String fillInstrId = preform.getFillInstrId();
				bo = annexSv.getAnnexById(fillInstrId);
			}else {
				//默认fixed 固定高度预览
				bo = annexSv.getAnnexById(preform.getHtml());
			}
		}

		if(bo == null){
//			PreformUtils.notFoundPreformError(httpResponse,"Mysql文件模板附件为空!");//找不到文件模板
			throw new Exception("Mysql文件模板附件为空!");
		}

		String filePath = bo.getPath() + File.separator + bo.getId();
		File file = new File(filePath);
		
		//去老路径查找文件
		if(!file.exists()){
			IFormModelDao formModelDao = (IFormModelDao) svF.getService(IFormModelDao.class);
			Map<String, String> map = new HashMap<String, String>();
			map.put("formcode", "TfPreform");
			map.put("curcode", "file");

			List<FormElmParModel> list = formModelDao.queryFormElePar(map);
			if (list == null || list.size() == 0) {
//				PreformUtils.notFoundPreformError(httpResponse,"文件模板参数配置为空!");
				throw new Exception("文件模板参数配置为空!");
			}
			FormElmParModel parModel = list.get(0);
			String fileStr = parModel.getThisValue();
	 		filePath = fileStr + File.separator + bo.getId();
	 		
	 		file = new File(filePath);
	 		if(!file.exists()){
//	 			PreformUtils.notFoundPreformError(httpResponse,"文件模板在指定位置不存在!");
				throw new Exception("文件模板在指定位置不存在!");
	 		}
		}
		
		
		
		Date end = new Date();
		log.error("TfPreformAction viewPreform2总耗时:"
				+ (end.getTime() - start.getTime()) / 1000 + "秒");
		
		return file;
	}

	/**
	 * 根据mongodb id获得数据
	 * @param httpResponse
	 * @param _id
	 * @return
	 * @throws Exception
	 */
	@Override
	public DBObject findMongoDataById(String _id) throws Exception {
		DBObject result;
		IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.put("_id", new ObjectId(_id));
//		result = mongodb.findById("preformData", dbObj);//TODO
		result = new BasicDBObject();
		if (result == null) {
			return null;
		}
		return result;
	}

	/**
	 * 预览空页面 (ie8跨域的默认页面,解决ie8跨域权限问题)
	 * @param httpRequest
	 * @param httpResponse
	 * @return
	 * @throws Exception
	 */
	public String viewEmptyPreform(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws Exception {
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setCharacterEncoding("utf-8");
		httpResponse.setContentType("text/html;charset=utf-8");
		PrintWriter pw = httpResponse.getWriter();
		String domain = PreformConfig.getInstance().getConfig(PreformConfig.DOMAIN);//10333.com
		String domainContent = String.format("<SCRIPT type='text/javascript'>document.domain='%s';</SCRIPT>",domain);
		try {
			pw.print("<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /></head>" + domainContent 
				+ "<body></body></html>");
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		return "";
	}

	/**
	 * 替换domain
	 * @param htmlStr http响应body
	 * @param expectDomain 期望的domain
	 * @return
	 */
	private String replaceDomain(String htmlStr, String expectDomain) {
		int startIndex = htmlStr.indexOf("<SCRIPT type='text/javascript'>document.domain=");
		int endQuote = htmlStr.indexOf("</SCRIPT>", startIndex);
		if(startIndex == -1){
			return htmlStr;
		}
		String oldDomain = htmlStr.substring(startIndex, endQuote + 9);
		htmlStr = htmlStr.replace(oldDomain, expectDomain);
		return htmlStr;
	}
	
	/**
	 * 测试环境增加保存按钮
	 * @param htmlStr html页面
	 * @return
	 */
	private String addSaveBtn(String htmlStr) {
		int bodyEndIndex = htmlStr.indexOf("<!--endprint1-->");
		if(bodyEndIndex == -1){
			return htmlStr;
		}
		htmlStr = htmlStr.replace("<!--endprint1-->", "<!--endprint1-->" + 
				"<div id='dlg-buttons' style='margin-bottom: 5px;margin-left: 5cm;margin-top: 20px;'><input type='submit' value='保存' onclick='saveData();   '/> </div>" + 
				"<div id='dlg-buttons' style='margin-bottom: 5px;margin-left: 5cm;margin-top: 20px;'><input type='submit' value='预览' onclick='viewPreform();'/> </div>");
		return htmlStr;
	}
	
	/**
	 * 处理本地测试的情况
	 * 1.去掉domain.
	 * 2.增加 保存按钮.
	 * @param rlsStr
	 * @param newDomain
	 * @return
	 */
	private String handleLocalTest(String rlsStr) {
		rlsStr = replaceDomain(rlsStr,"");
		rlsStr = addSaveBtn(rlsStr);
		return rlsStr;
	}

	/**
	 * 处理文件流转
	 * 解决问题:
	 * 		输入控件有值且不属于当前机构,表明当前机构对此输入的数据没有修改权限,那么将此输入控件转变为只可预览
	 * @param _id
	 * @param orgId
	 * @param html
	 * @param result
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private String handleFlow(String _id, String orgId, String html,
			DBObject result) throws Exception {
		if (StringsUtils.isEmpty(_id)) {
			return html;
		}
		List<String> orgValue = null;
		if (!StringsUtils.isEmpty(orgId) && result.get(orgId) != null) {
			orgValue = (List<String>) result.get(orgId);
		}
		Pattern pattern = Pattern.compile("^A\\d*$");// 判断是否是 A0 A1 A2 A3
		List<String> list = new ArrayList<String>();
		for (String dkey : result.keySet()) {
			boolean flag = pattern.matcher(dkey).matches();
			if(!flag){
				continue;
			}
			// 这里判断当前orgId是否有权限
			if (orgValue != null) {//orgValue 是当前org所拥有的控件
				if (!orgValue.contains(dkey)) {
					list.add(dkey);
				}
			}else{
				list.add(dkey);
			}
		}
		html = editConver2View(html, list);
		return html;
	}

	/**
	 * 将传递的参数值保存在隐藏域
	 * @param html
	 * @param paramValue
	 * @return
	 */
	private String setParamValue(String html, String paramValue) {
		JSONObject jsonObject = JSONObject.fromObject(paramValue);
		html = html.replace("<input id='paramValue' name='paramValue' type='hidden'>", 
				String.format("<input id='paramValue' name='paramValue' type='hidden' value='%s'>",jsonObject.toString()));
		return html;
	}

	/**
	 * 将指定的可编辑控件转换为预览控件
	 * @param html
	 * @param list
	 * @return
	 */
	private String editConver2View(String html, List<String> list) {
		for (String inputId : list) {
			String textStr = String.format(
					"<input id='%s' name='%s' type='text'", inputId, inputId); //<input id='A0' name='A0' type='text'
																				
			String textAreaStr = String.format(
			"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='true' showmenu='true'",
					inputId, inputId); //<textarea id='A0' name='A0'
																				
			String editorStr = String .format( "<div id='%s' name='%s' isEditor='true' contenteditable='true'",
					inputId, inputId); //<div id='A0' name='A0'
			
			String chkStr = String.format(
					"<input id='%s' name='%s' type='checkbox' alias='", inputId, inputId); //<input id='A0' name='A0' type='checkbox'
			
			String orgProStr = String.format("<div id='%s' name='%s' widgeType=", inputId, inputId); //所有div
			
			//处理参建方控件的下拉框
			String selectStr = String.format("<select id='%ss' name='%ss'", inputId, inputId);
			
			int inputStart = -1;
			
			if ((inputStart = html.indexOf(textStr)) != -1) {// 处理input
				int inputEnd = html.indexOf(">", inputStart);
				String inputHtml = html.substring(inputStart, inputEnd + 1);
				String spanHtml = inputHtml.replace("<input", "<span").replace(
						"/>", "></span>").replace("border", "noborder");
				//特殊控件处理
				spanHtml = spanHtml.replace("ondblclick=\"dblclickEvent",
						"ondblclick1=\"dblclickEvent");
				//背景色处理
				spanHtml =spanHtml.replace("background-color","background-color1");
				html = html.replace(inputHtml, spanHtml);
			} else if ((inputStart = html.indexOf(textAreaStr)) != -1) {// 处理多行输入框
				String strEnd="contenteditable='true' showmenu='true'";
				String replaceStrEnd="contenteditable='false' showmenu='false'";
				int inputEnd = html.indexOf(strEnd, inputStart);
				String inputHtml = html.substring(inputStart, inputEnd + strEnd.length());
				String spanHtml = inputHtml.replace(strEnd, replaceStrEnd);
				html = html.replace(inputHtml, spanHtml);
			} else if ((inputStart = html.indexOf(editorStr)) != -1) {// 处理编辑器
				String newEditorStr = String.format("<div id='%s' name='%s' isEditor='true' ", inputId,inputId);
				html = html.replace(editorStr, newEditorStr);
			} else if((inputStart = html.indexOf(chkStr)) != -1){//处理单选框
				String str = html.substring(inputStart);
				String alias =  str.substring(str.indexOf("alias='") + 7, str.indexOf("' enabled='enabled'"));
				if(!StringUtils.isEmpty(alias)){
					html = html.replaceAll(String.format("type='checkbox' alias='%s' enabled='enabled'", alias), String.format("type='checkbox' alias='%s' disabled='disabled'", alias));
				}
			}else if ((inputStart = html.indexOf(orgProStr)) != -1) {// 处理当前项目和机构
				String strEnd="contenteditable='true'";
				String replaceStrEnd="contenteditable='false'";
				html = html.replace( String.format("border: 1px solid #ccc;overflow: hidden;'><div id='%s' name='%s'", inputId, inputId) , 
						String.format("border: 0px solid #ccc;overflow: hidden;'><div id='%s' name='%s'", inputId, inputId));
				int inputEnd = html.indexOf(strEnd, inputStart);
				if(inputEnd == -1){
					break;//当contenteditable处理完毕,中断循环
				}
				
				//判断contenteditable 是否为false
				int curConEdtIndex = html.indexOf("contenteditable='",inputStart);
				String curConEdt = html.substring(curConEdtIndex + 17 , curConEdtIndex + 17 + 5);//18为"contenteditable='"的长度,5为"false"长度
				if(curConEdt.startsWith("false")){
					continue;//当contenteditable='false'跳出本次循环
				}
				
				String inputHtml = html.substring(inputStart, inputEnd + strEnd.length());
				String spanHtml = inputHtml.replace(strEnd, replaceStrEnd);
				html = html.replace(inputHtml, spanHtml);
			}
			
			//处理参建方控件的样式
			if((inputStart = html.indexOf(selectStr)) != -1){
				int divStart = html.lastIndexOf("<div class='ptcp-div' ", inputStart);
				int divEnd = html.indexOf("</div>", divStart);
				String divStr = html.substring(divStart, divEnd + 6);
				int spanStart = divStr.indexOf("<span");
				int spanEnd = divStr.indexOf("></span>",spanStart);
				String spanStr = divStr.substring(spanStart, spanEnd + 8);
				spanStr = spanStr.replace("onpropertychange", "onpropertychange1").replace("oninput", "oninput1")
					.replace("ptcp-input", "ptcp-input1");
				html = html.replace(divStr, spanStr);
			}
		}
		return html;
	}
}
