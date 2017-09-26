package com.dotoyo.dsform.util;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.Environment.Entry;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.MessageBo;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameMessage;

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
}
