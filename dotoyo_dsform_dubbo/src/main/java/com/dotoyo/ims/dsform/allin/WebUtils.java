package com.dotoyo.ims.dsform.allin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.IModel;
import com.dotoyo.dsform.model.MessageBo;
import com.dotoyo.dsform.util.StringsUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * 
 * @author xieshh
 * 
 */
public class WebUtils {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(WebUtils.class));

	private WebUtils() {

	}

	/**
	 * 取得当前肤色
	 * 
	 * @return
	 */
	final public static String getSkin() {
		String skin = "";
		try {

			Map<String, String> param = new HashMap<String, String>();

			IFrameSkin factory = FrameFactory.getSkinFactory(param);
			skin = factory.getCurrentSkin();
			if (IFrameSkin.skins[0].equals(skin)) {
				skin = "default";
			} else if (IFrameSkin.skins[1].equals(skin)) {
				skin = "gray";
			} else if (IFrameSkin.skins[2].equals(skin)) {
				skin = "gray";
			} else {
				skin = "gray";
			}
		} catch (Throwable e) {
			log.error("", e);
		}
		return skin;
	}

	/**
	 * 取得当前肤色
	 * 
	 * @return
	 */
	final public static String getSkin4My97DatePicker() {
		String skin = "";
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("skin", "");
			IFrameSkin factory = FrameFactory.getSkinFactory(param);
			String code = factory.getCurrentSkin();
			if (IFrameSkin.skins[0].equals(code)) {
				skin = code;
			} else if (IFrameSkin.skins[1].equals(code)) {
				skin = code;
			}
		} catch (Throwable e) {
			log.error("", e);
		}
		if (StringsUtils.isEmpty(skin)) {
			skin = "default";
		}
		return skin;
	}

	/**
	 * 取得当前肤色
	 * 
	 * @return
	 */
	final public static String getSkin4ligerui() {
		String skin = "";
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("skin", "");
			IFrameSkin factory = FrameFactory.getSkinFactory(param);
			String code = factory.getCurrentSkin();

			skin = code;

		} catch (Throwable e) {
			log.error("", e);
		}
		if (StringsUtils.isEmpty(skin)) {
			skin = "Aqua";
		}
		return skin;
	}

	/**
	 * 取得当前字符集
	 * 
	 * @return
	 */
	final public static String getLanguage() {
		String lang = "";
		try {

			Map<String, String> param = new HashMap<String, String>();
			param.put("language", IFrameLanguage.types[0]);
			IFrameLanguage factory = FrameFactory.getLanguageFactory(param);
			String code = factory.getCode();
			if (IFrameLanguage.types[0].equals(code)) {
				lang = "zh_CN";
			} else if (IFrameLanguage.types[1].equals(code)) {
				lang = code;
			} else if (IFrameLanguage.types[2].equals(code)) {
				lang = "zh_TW";

			}
		} catch (Throwable e) {
			log.error("", e);
		}
		return lang;
	}

	/**
	 * 取得当前字符集，用于My97DatePicker组件
	 * 
	 * @return
	 */
	final public static String getLanguage4My97DatePicker() {
		String lang = "";
		try {

			Map<String, String> param = new HashMap<String, String>();
			param.put("language", IFrameLanguage.types[0]);
			IFrameLanguage factory = FrameFactory.getLanguageFactory(param);
			String code = factory.getCode();
			if (IFrameLanguage.types[0].equals(code)) {
				lang = "zh-cn";
			} else if (IFrameLanguage.types[1].equals(code)) {
				lang = code;
			} else if (IFrameLanguage.types[2].equals(code)) {
				lang = "zh-tw";

			}
		} catch (Throwable e) {
			log.error("", e);
		}
		return lang;
	}

	/**
	 * 取得从请求中取得报表参数
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static ReportDataRequestBo getReportDataRequestBo(
			HttpServletRequest httpRequest) throws Exception {

		ReportDataRequestBo bo = new ReportDataRequestBo();

		String code = httpRequest.getParameter("reportCode") + "";
		if (StringsUtils.isEmpty(code)) {
			// 参数不能为空
			FrameException bte = new FrameException("3000000000000001", null);
			throw bte;
		}
		String page = httpRequest.getParameter("page") + "";
		if (StringsUtils.isEmpty(page)) {
			page = "1";
		} else {
			if (StringsUtils.getLong(page) == 0 || !StringsUtils.isNumber(page)) {
				page = "1";
			}
		}
		String szie = httpRequest.getParameter("rows") + "";
		if (StringsUtils.isEmpty(szie)) {
			szie = "10";
		} else {
			if (StringsUtils.getLong(szie) == 0 || !StringsUtils.isNumber(szie)) {
				szie = "10";
			}
		}
		String time = httpRequest.getParameter("time") + "";
		if (StringsUtils.isEmpty(time)) {
			time = (System.currentTimeMillis() - 1000 * 4) + "";
		} else {
			if (StringsUtils.getLong(time) == 0 || !StringsUtils.isNumber(time)) {
				time = (System.currentTimeMillis() - 1000 * 4) + "";
			}
		}
		String count = httpRequest.getParameter("count") + "";
		if (StringsUtils.isEmpty(count)) {
			count = "0";
		} else {
			if (StringsUtils.getLong(count) == 0
					|| !StringsUtils.isNumber(count)) {
				count = "0";
			}
		}
		String sort = httpRequest.getParameter("sort") + "";
		String order = httpRequest.getParameter("order") + "";
		String params = "";
		if (httpRequest instanceof FrameRequestWrapper) {
			FrameRequestWrapper new_name = (FrameRequestWrapper) httpRequest;
			params = new_name.getParameterFromParent("query");
		} else {
			params = httpRequest.getParameter("query");
		}
		if (StringsUtils.isEmpty(params)) {
			params = "{}";
		}
		bo.setCode(code);
		bo.setPage(page);
		bo.setSzie(szie);
		bo.setTime(time);
		bo.setCount(count);
		bo.setSort(sort);
		bo.setOrder(order);
		// String str = URLDecoder.decode(URLDecoder.decode(params, "utf-8"),
		// "utf-8");
		// testStringEncoding(str );
		String str = params;
		JSONObject obj = JSONObject.fromObject(str);
		fixJSONObject4Web(obj);
		bo.setParams(obj);
		return bo;

	}

	protected static void fixJSONObject4Web(JSONObject json) throws Exception {
		for (Object key : json.keySet()) {
			Object obj = json.get(key);
			if (obj instanceof JSONObject) {
				JSONObject new_name1 = (JSONObject) obj;
				fixJSONObject4Web(new_name1);
			} else if (obj instanceof JSONArray) {
				JSONArray new_name1 = (JSONArray) obj;
				fixJSONArray4Web(new_name1);
			} else if (obj instanceof String) {
				String value = (String) obj;
				String str = URLDecoder.decode(URLDecoder
						.decode(value, "utf-8"), "utf-8");
				IFrameXss xss = FrameXss.getInstance(null);
				String encodeStr = xss.xss(str);
				json.put(key, encodeStr);
				if (SecurityUtil.doSqlValidate(str, false)) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("param", encodeStr);
					m.put("name", key + "");
					FrameException e = new FrameException("3000000000000028", m);
					throw e;
				}
			} else {

			}
		}
	}

	protected static void fixJSONArray4Web(JSONArray array) throws Exception {
		Object[] a = array.toArray();
		for (int i = 0; i < a.length; i++) {
			Object obj = a[i];
			if (obj instanceof JSONObject) {
				JSONObject new_name1 = (JSONObject) obj;
				fixJSONObject4Web(new_name1);
			} else if (obj instanceof JSONArray) {
				JSONArray new_name1 = (JSONArray) obj;
				fixJSONArray4Web(new_name1);
			} else if (obj instanceof String) {
				String value = (String) obj;
				String str = URLDecoder.decode(URLDecoder
						.decode(value, "utf-8"), "utf-8");
				IFrameXss xss = FrameXss.getInstance(null);
				String encodeStr = xss.xss(str);
				a[i] = encodeStr;
				if (SecurityUtil.doSqlValidate(str, false)) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("param", encodeStr);

					FrameException e = new FrameException("3000000000000028", m);
					throw e;
				}
			} else {

			}
		}

	}

	/**
	 * 
	 * 取得从请求中取模型对象
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static IModel getModelFromRequest4NoDecode(
			HttpServletRequest httpRequest, Class<?> cls) throws Exception {
		JSONObject ret = getJsonFrameRequest4NoDecode(httpRequest);
		@SuppressWarnings("unchecked")
		IModel obj = (IModel) ClassUtils.map2Bean(ret, cls);
		return obj;
	}

	/**
	 * 
	 * 取得从请求中取模型对象
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static IModel getModelFromRequest(HttpServletRequest httpRequest,
			Class<?> cls) throws Exception {
		JSONObject ret = getJsonFrameRequest(httpRequest);
		@SuppressWarnings("unchecked")
		IModel obj = (IModel) ClassUtils.map2Bean(ret, cls);
		return obj;
	}

	/**
	 * 
	 * 取得JSON中取模型
	 * 
	 * @param JSONObject
	 * @return
	 */
	public static IModel jsonToModel(JSONObject jobj, Class<?> cls)
			throws Exception {
		String[] dateFormats = new String[] { "yyyy-MM-dd HH:mm:ss",
				"yyyy-MM-dd" };
		TimestampMorpher tm = new TimestampMorpher(dateFormats);
		JSONUtils.getMorpherRegistry().registerMorpher(tm);
		DateMorpher dm = new DateMorpher(dateFormats);
		JSONUtils.getMorpherRegistry().registerMorpher(dm);
		IModel model = (IModel) JSONObject.toBean(jobj, cls);

		return model;
	}

	/**
	 * 
	 * 取得JSON中取模型
	 * 
	 * @param JSONObject
	 * @return
	 */
	public static JSONObject modelToJson(IModel model) throws Exception {

		JsonConfig cfg = new JsonConfig();
		JsonDateValue2StringProcessor pro = new JsonDateValue2StringProcessor();
		cfg.registerJsonValueProcessor(Date.class, pro);
		JsonTimeValue2StringProcessor timePro = new JsonTimeValue2StringProcessor();
		cfg.registerJsonValueProcessor(Timestamp.class, timePro);

		JSONObject ret = JSONObject.fromObject(model, cfg);

		return ret;
	}

	/**
	 * 
	 * 取得从请求中取JSON参数
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static JSONObject getJsonFrameRequest(HttpServletRequest httpRequest)
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
			log.debug(str);
		}

		JSONObject ret = JSONObject.fromObject(str);
		fixJSONObject4Web(ret);
		return ret;

	}
	
	
	/**
	 * 
	 * 取得从请求中取JSON参数,不做xss检查（此方法只给 编辑器使用）
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static JSONObject getJsonFrameRequest4Ckeditor(HttpServletRequest httpRequest)
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
			log.debug(str);
		}
		str = URLDecoder.decode(URLDecoder.decode(str.toString(), "utf-8"),"utf-8");
		log.debug("getJsonFrameRequest4Ckeditor str :" + str);
		JSONObject ret = JSONObject.fromObject(str);
		return ret;

	}
	
	/**
	 * 从请求中取消息
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static Map<String, Object> getMessage(HttpServletRequest httpRequest)
			throws Exception {
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		return msgF.getReqMessage(httpRequest);

	}

	/**
	 * 取得从请求中取JSON参数
	 * 
	 * @param httpRequest
	 * @return
	 */
	protected static JSONObject getJsonFrameRequest4NoDecode(
			HttpServletRequest httpRequest) throws Exception {

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

		log.debug(str);
		JSONObject ret = JSONObject.fromObject(str);

		return ret;

	}

	/**
	 * 反回客户端
	 * 
	 * @param response
	 * @param bo
	 * @throws Exception
	 */
	public static void reportDataResponse(HttpServletResponse response,
			ReportDataRequestBo bo) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			List<Map<String, String>> list = bo.getList();
			IFrameMessage msgF = FrameFactory.getMessageFactory(null);
			MessageBo msgBo = new MessageBo();
			msgBo.setRespType("json");
			String ret = msgF.getRespMessage(msgBo, list, StringsUtils
					.getLong(bo.getCount()));
			log.debug(ret);
			pw.print(ret);
			pw.flush();
			// ret.write();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
	 * 反回客户端
	 * 
	 * @param response
	 * @param bo
	 * @throws Exception
	 */
	public static void reportDataResponseError(HttpServletResponse response,
			Throwable e) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		PrintWriter pw = null;
		try {
			IFrameMessage msgF = FrameFactory.getMessageFactory(null);
			MessageBo bo = new MessageBo();
			bo.setRespType("json");
			pw = response.getWriter();
			String json = msgF.getRespMessage4List(bo, e);
			log.debug(json);
			pw.print(json);
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
	public static void responseJsonSuccess(HttpServletResponse response,
			PageListModel model) throws Exception {
		// response.setCharacterEncoding("UTF-8");
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		String str = msgF.getRespMessage(bo, model);
		log.debug(str);
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
	public static void responseJsonSuccess(HttpServletResponse response)
			throws Exception {
		// response.setCharacterEncoding("UTF-8");
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		String str = msgF.getRespMessage(bo);
		log.debug(str);
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
	@SuppressWarnings("unchecked")
	public static void responseJsonSuccess(HttpServletResponse response,
			List list, long total) throws Exception {

		PageListModel model = new PageListModel(total, list);
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		String str = msgF.getRespMessage(bo, model);
		log.debug(str);
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

	public static void responseJsonSuccess(HttpServletResponse response,
			String json, long total) throws Exception {

		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");

		String str = msgF.getRespMessage(bo, json, total);
		log.debug(str);
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
	public static void responseJsonSuccess(HttpServletResponse response,
			JSONObject json) throws Exception {
		// response.setCharacterEncoding("UTF-8");

		
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, json);
		log.debug(str);
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
	public static void responseJsonSuccess(HttpServletResponse response,
			JSONArray jsonArray) throws Exception {
		// response.setCharacterEncoding("UTF-8");

		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, jsonArray);
		log.debug(str);
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
	public static void responseJsonSuccess(HttpServletResponse response,
			IModel model) throws Exception {
		// response.setCharacterEncoding("UTF-8");

		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, model);
		log.debug(str);
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
	public static void responseJsonSuccess(HttpServletResponse response,
			JSONObject json, String code) throws Exception {
		// response.setCharacterEncoding("UTF-8");

		String msg = FrameUtils.getWords(code, "", null);

		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, json, code, msg);
		log.debug(str);
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
	public static void responseJsonError(HttpServletResponse response,
			Throwable e) throws Exception {
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, e);
		log.debug(str);
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
	public static void responseJsonError(HttpServletResponse response,
			Throwable e, JSONObject json) throws Exception {
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, e, json);
		log.debug(str);
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
	 * 取得从请求中取得报表参数
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static String getColumnJsonString(ReportRequestBo bo) {
		JSONArray ret = new JSONArray();
		List<ReportElementRequestBo> list1 = bo.getColumn1();
		if (list1 != null && list1.size() > 0) {
			JSONArray l1 = new JSONArray();
			for (int i = 0; i < list1.size(); i++) {
				ReportElementRequestBo elBo = list1.get(i);
				JSONObject obj = JSONObject.fromObject(elBo);
				l1.add(obj);
			}
			ret.add(l1);
		}
		List<ReportElementRequestBo> list2 = bo.getColumn2();
		JSONArray l2 = new JSONArray();
		for (int i = 0; i < list2.size(); i++) {
			ReportElementRequestBo elBo = list2.get(i);
			JSONObject obj = JSONObject.fromObject(elBo);
			// 设置隐藏域
			if ("hidden".equals(elBo.getElType().toLowerCase())) {
				obj.put("hidden", true);
			}
			l2.add(obj);
		}

		ret.add(l2);
		// html.append("var columns=[[");
		// html.append(" {title:'Base Information',colspan:3},");
		// html.append("
		// {field:'opt',title:'Operation',width:100,align:'center',
		// rowspan:2,");
		// html.append(" formatter:function(value,rec){");
		// html.append(" return '<span style=\"color:red\">Edit
		// Delete</span>';");
		// html.append(" }");
		// html.append(" }");
		// html.append(" ],[");
		// html.append(" {field:'name',title:'Name',width:120},");
		// html.append("
		// {field:'addr',title:'Address',width:220,rowspan:2,sortable:true,");
		// html.append(" sorter:function(a,b){");
		// html.append(" return (a>b?1:-1);");
		// html.append(" }");
		// html.append(" },");
		// html.append(" {field:'col4',title:'Col41',width:150,rowspan:2}");
		// html.append(" ]];");
		String retStr = ret.toString();
		return retStr;
	}

	public static String getLockColumnJsonString(ReportRequestBo bo) {
		JSONArray ret = new JSONArray();
		JSONArray l1 = new JSONArray();
		JSONObject objCheck = JSONObject
				.fromObject("{field:'ck',checkbox:true}");
		l1.add(objCheck);
		List<ReportElementRequestBo> list1 = bo.getLockColumn();
		for (int i = 0; i < list1.size(); i++) {
			ReportElementRequestBo elBo = list1.get(i);
			JSONObject obj = JSONObject.fromObject(elBo);
			l1.add(obj);
		}

		ret.add(l1);

		String retStr = ret.toString();
		return retStr;
		// html.append(" var frozenColumns=[[");
		// html.append(" {field:'ck',checkbox:true},");
		// html.append(" {title:'code',field:'code',width:80,sortable:true}");
		// html.append(" ]];");
		// return "";
	}

	/**
	 * 取得当前访问的URL地址，不包括根路径
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static String getVisitUrl4NoRoot(HttpServletRequest httpRequest) {
		String curUrl = httpRequest.getRequestURI();
		String root = httpRequest.getContextPath();
		curUrl = curUrl.substring(root.length(), curUrl.length());
		return curUrl;
	}

	/**
	 * @deprecated
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getRequestData(
			HttpServletRequest httpRequest) throws Exception {
		FrameException e = new FrameException("3000000000000006", null);
		throw e;

	}

	/**
	 * 判断当前URL是否是资源
	 * 
	 * @param requestURI
	 * @return
	 * @throws Exception
	 */
	public static boolean isResource(HttpServletRequest httpRequest)
			throws Exception {
		String requestURI = getVisitUrl(httpRequest);
		if (StringUtils.isEmpty(requestURI)) {
			return false;
		}
		String curUrl = requestURI.trim().toLowerCase();
		if (curUrl.endsWith(".css") || curUrl.endsWith(".js")) {
			return true;
		}
		return isResource4Config(curUrl);
	}

	/**
	 * 取访问URL地址
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public static String getVisitUrl(HttpServletRequest httpRequest)
			throws Exception {

		String curUrl = WebUtils.getVisitUrl4NoRoot(httpRequest);
		int id = curUrl.indexOf(';');
		if (id != -1) {
			curUrl = curUrl.substring(0, id);
		}
		id = curUrl.indexOf(':');
		if (id != -1) {
			curUrl = curUrl.substring(0, id);
		}
		id = curUrl.indexOf('?');
		if (id != -1) {
			curUrl = curUrl.substring(0, id);
		}
		return curUrl;
	}

	/**
	 * 判断当前URL是否是资源
	 * 
	 * @param requestURI
	 * @return
	 * @throws Exception
	 */
	public static boolean isResource(String requestURI) throws Exception {

		if (StringUtils.isEmpty(requestURI)) {
			return false;
		}
		String curUrl = requestURI.trim().toLowerCase();
		if (curUrl.endsWith(".css") || curUrl.endsWith(".js")) {
			return true;
		}
		return isResource4Config(curUrl);
	}

	/**
	 * 根据数据库配置判断当前URL是否是资源
	 * 
	 * @param curUrl
	 * @return
	 * @throws Exception
	 */
	public static boolean isResource4Config(String curUrl) throws Exception {
		if (StringUtils.isEmpty(curUrl)) {
			return false;
		}
		String requestURI = curUrl.trim().toLowerCase();
		if (!StringUtils.isEmpty(requestURI)) {
			Map<String, String> param = new HashMap<String, String>();
			IFrameKeyvalue factory = FrameFactory.getKeyValueFactory(param);
			List<Map<String, String>> urlmap = factory.getKeyValueList(
					"security", "004");
			if (urlmap == null || urlmap.size() < 0) {
				return false;
			}
			String value = null;
			for (Map<String, String> map : urlmap) {
				if (map == null) {
					continue;
				}
				value = map.get("VALUE");
				if (StringsUtils.isEmpty(value)) {
					continue;
				}
				value = value.trim().toLowerCase();
				if (requestURI.endsWith(value)) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * 从请求中取得查询参数
	 * 
	 * @deprecated
	 * @param httpRequest
	 * @return
	 */
	public static Object getFormBean(HttpServletRequest httpRequest,
			Class<?> class1) throws Exception {
		Object bean = class1.newInstance();
		Method[] methods = class1.getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String field = methodName.substring(3);
				String fieldName = field.substring(0, 1).toLowerCase()
						+ field.substring(1);

				String value = httpRequest.getParameter(fieldName);
				if (!StringsUtils.isNull(value)) {
					method.invoke(bean, value);
				}
			}
		}
		return bean;
	}

	/**
	 * @deprecated 从json中取得bean
	 */
	public static Object getAjaxBean(JSONObject reqJson, Class<?> class1)
			throws Exception {
		Object bean = class1.newInstance();
		Method[] methods = class1.getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String field = methodName.substring(3);
				String fieldName = field.substring(0, 1).toLowerCase()
						+ field.substring(1);

				String value = reqJson.optString(fieldName);
				if (!StringsUtils.isNull(value)) {
					method.invoke(bean, value);
				}
			}
		}
		return bean;
	}

	/**
	 * 取得当前系统标题
	 * 
	 * @return
	 */
	final public static String getSystemTitle() {
		String keyvalue = "FRAME";
		try {
			Map<String, String> param = new HashMap<String, String>();

			IFrameKeyvalue factory = FrameFactory.getKeyValueFactory(param);
			String code = "systemTitle";
			String module = "web";
			keyvalue = factory.getKeyValue(module, code);
			if (StringsUtils.isEmpty(keyvalue)) {
				keyvalue = "FRAME";
			}
		} catch (Throwable e) {
			log.error("", e);
			keyvalue = "FRAME";
		}
		return keyvalue;
	}

	/**
	 * @deprecated 将容易引起xss漏洞的半角字符直接替换成全角字符
	 * 
	 * @param s
	 * @return
	 */
	public static String doXssEncode4Attribute(HttpServletRequest request,
			String key) {
		try {
			String value = (String) request.getAttribute(key);
			if (StringsUtils.isEmpty(value)) {
				return "";
			}
			String msg = StringsUtils.doXssEncode(value);
			return msg;
		} catch (Throwable e) {
			log.error("", e);
		}
		return "";
	}

	/**
	 * 设置cookie
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, String path, int maxAge) {

		Cookie cookie = new Cookie(name, value);

		if (StringsUtils.isEmpty(path)) {
			cookie.setPath("/");
		} else {
			cookie.setPath(path);
		}
		if (maxAge > 0) {
			cookie.setMaxAge(maxAge);
		}
		response.addCookie(cookie);
	}

	/**
	 * 根据名字获取cookie
	 * 
	 * @param request
	 * @param name
	 *            cookie名字
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (StringsUtils.isEmpty(name)) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static String urlEncode(String url) {
		String str = "";
		try {
			str = URLEncoder.encode(url, "utf-8");

		} catch (Throwable e) {
			log.debug("", e);
		}
		return str;
	}

	public static void addHttpHead(String key, String value,
			Map<String, List<String>> headList) {

		List<String> list = headList.get(key);
		if (list == null) {
			list = new ArrayList<String>();
			headList.put(key, list);
		}
		list.add(value);

	}

	@SuppressWarnings("unchecked")
	public static void getRequestPatameters(HttpServletRequest httpRequest,JSONObject reqJson) {
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
}
