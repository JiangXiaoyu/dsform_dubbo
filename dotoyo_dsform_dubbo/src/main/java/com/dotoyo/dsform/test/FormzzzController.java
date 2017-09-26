package com.dotoyo.dsform.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dotoyo.dsform.interf.IFormInstanceService;
import com.dotoyo.dsform.interf.ITfFrameMessageService;
import com.dotoyo.dsform.interf.ITfPreformService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/formzzz")
public class FormzzzController {
	
//	extends OrgBaseController
	
	@Resource(name = "formFormInstanceService_form")
	IFormInstanceService formInstanceSerivce;
	
	@Resource(name = "formTfFrameMessageService_form")	
	public ITfFrameMessageService  iTfFrameMessageService;	
	
	@Resource(name = "formTfPreformService_form")	
	public ITfPreformService  iTfPreformService;

	/**
	 * 获得指定预定义报表的填写的数据
	 * @param httpRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getPreformData")
	public String getPreformData(HttpServletRequest httpRequest,
			HttpServletResponse response) throws Exception {
		try {
			Date start = new Date();
			response.setHeader("Access-Control-Allow-Origin", "*");// 以后修改，这里有安全问题
			String _id = httpRequest.getParameter("_id");
//			JSONObject reqJson = new JSONObject();
//			reqJson.put("_id", _id);
//			IFrameServiceLog logF = FrameFactory.getServiceLogFactory(null);
//			logF.addServiceLog(IFrameServiceLog.QUERY, this.getClass(),
//					"getPreformData", reqJson.toString());
//			IFrameService fs = FrameFactory.getServiceFactory(null);
//			ITfPreformService sv = (ITfPreformService) fs
//					.getService(TfPreformService.class.getName());
//			JSONObject json = sv.getPreformData4Mongodb(reqJson);
			
			JSONObject json = formInstanceSerivce.getPreformData(_id);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			this.responseJsonSuccess(response, json);
			Date end = new Date();
//			log.error("getPreformData 耗时:" + (end.getTime() - start.getTime())
//					/ 1000 + "秒");
		} catch (Exception e) {
//			log.error("", e);
			this.responseJsonError(response, e);
		}
		return "";
	}
	
	
	
	
	
	
		
	
	
	

	/**
	 * 通过ip获得位置(sina接口),通过位置获得天气(baidu接口)
	 * @param httpRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getWeaDataByIp(HttpServletRequest httpRequest,
			HttpServletResponse response) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");// 以后修改，这里有安全问题
		JSONObject obj = new JSONObject();//最终返回的结果
		try {
			String ip = httpRequest.getParameter("ip");
			String weatherStr = formInstanceSerivce.getData(ip);
			
			JSONObject weatherRlt = JSONObject.fromObject(weatherStr);
			JSONArray waatherRltArray = weatherRlt.optJSONArray("results");//天气的查询结果
			if (waatherRltArray != null && waatherRltArray.size() != 0) {
				JSONObject weatherObj = (JSONObject) waatherRltArray.get(0);
				JSONArray weaArray = weatherObj.optJSONArray("weather_data");
				if (weaArray.size() != 0) {
					JSONObject wea = (JSONObject) weaArray.get(0);
					String resu = wea.optString("weather");
					obj.put("result", resu);
				}
			}
			this.responseJsonSuccess(response, obj);
		} catch (Exception e) {
//			log.error("", e);
			this.responseJsonSuccess(response, new JSONObject());
		}
		return "";
	}
	
	
	
	/**
	 * 
	 * 取得从请求中取JSON参数
	 * 
	 * @param httpRequest
	 * @return
	 */
	public JSONObject getJsonFrameRequest(HttpServletRequest httpRequest)
			throws Exception {

		//
		InputStream inputStream = null;
		Reader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			inputStream = httpRequest.getInputStream();
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {

				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {

				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {

				}
			}
		}

		if (sb.length() <= 0) {
			sb.append("{}");
		}
		String str = sb.toString();
		// str = URLDecoder.decode(URLDecoder.decode(str, "utf-8"), "utf-8");
		if (!"{}".equals(str)) {
//			log.debug(str);
		}

		JSONObject ret = JSONObject.fromObject(str);
		iTfFrameMessageService.fixJSONObject4Web(ret);
		return ret;

	}
	
	@SuppressWarnings("unchecked")
	public void getRequestPatameters(HttpServletRequest httpRequest,JSONObject reqJson) {
		Map map =  httpRequest.getParameterMap();
		Set<Map.Entry> entrys = map.entrySet();
		for(Map.Entry en : entrys){
			String key = en.getKey().toString();
			String value = en.getValue() != null ? ((String[])en.getValue())[0] : "";
			if(!reqJson.has(key)){
				reqJson.put(key, value);
			}
		}
	}
	
	/**
	 * 
	 * 取得从请求中取JSON参数,不做xss检查（此方法只给 编辑器使用）
	 * 
	 * @param httpRequest
	 * @return
	 */
	public JSONObject getJsonFrameRequest4Ckeditor(HttpServletRequest httpRequest)
			throws Exception {

		//
		InputStream inputStream = null;
		Reader reader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			inputStream = httpRequest.getInputStream();
			reader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(reader);
			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {

				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {

				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {

				}
			}
		}

		if (sb.length() <= 0) {
			sb.append("{}");
		}
		String str = sb.toString();
		// str = URLDecoder.decode(URLDecoder.decode(str, "utf-8"), "utf-8");
		if (!"{}".equals(str)) {
//			log.debug(str);
		}
		str = URLDecoder.decode(URLDecoder.decode(str.toString(), "utf-8"),"utf-8");
//		log.debug("getJsonFrameRequest4Ckeditor str :" + str);
		JSONObject ret = JSONObject.fromObject(str);
		return ret;

	}
	
	/**
	 * 处理成功返回客户端
	 * 
	 * @param response
	 * @param bo
	 * @throws Exception
	 */
	public void responseJsonSuccess(HttpServletResponse response,
			JSONArray jsonArray) throws Exception {
		// response.setCharacterEncoding("UTF-8");

		String str = iTfFrameMessageService.getRespMessage(jsonArray);
//		log.debug(str);
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
	}
	
	/**
	 * 处理成功返回客户端
	 * 
	 * @param response
	 * @param bo
	 * @throws Exception
	 */
	public void responseJsonSuccess(HttpServletResponse response,
			JSONObject json) throws Exception {
		// response.setCharacterEncoding("UTF-8");	
		
		String str = iTfFrameMessageService.getRespMessage(json);
//		log.debug(str);
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
	}
	
	/**
	 * 把错误返回客户端
	 * 
	 * @param response
	 * @param bo
	 * @throws Exception
	 */
	
	public void responseJsonError(HttpServletResponse response,
			Throwable e) throws Exception {
		String str = iTfFrameMessageService.getRespMessage(e);
//		log.debug(str);
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
	}
	
	/**
	* 删除Html标签
	* @param inputString
	* @return
	*/
	public static String removeHtmlTag(String inputString) {
		if (inputString == null) {
			return null;
		}
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_special;
		java.util.regex.Matcher m_special;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			// 定义HTML标签的正则表达式
			String regEx_html = "<[^>]+>";
			// 定义一些特殊字符的正则表达式 如：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			String regEx_special = "\\&[a-zA-Z]{1,10};";
		
//			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
//			m_script = p_script.matcher(htmlStr);
//			htmlStr = m_script.replaceAll(""); // 过滤script标签
//			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
//			m_style = p_style.matcher(htmlStr);
//			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
			m_special = p_special.matcher(htmlStr);
			htmlStr = m_special.replaceAll(""); // 过滤特殊标签
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}
}
