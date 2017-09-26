package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 请求参数安全检查实现类
 * 
 * @author xieshh
 * 
 */
public class FrameRequestWrapper extends HttpServletRequestWrapper {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameRequestWrapper.class));
	private static Map<String, String> param = new HashMap<String, String>();
	static {
		param.put("accept-language", "");
		param.put("accept-charset", "");
		param.put("accept", "");
		param.put("accept-encoding", "");
		param.put("accept-agent", "");
		param.put("host", "");
		param.put("jsessionid", "");
		param.put("referer", "");
		param.put("connection", "");
		param.put("content-language", "");
		param.put("content-type", "");
		param.put("date", "");
		param.put("server", "");
		param.put("transfer-encoding", "");
		param.put("cookie", "");
		param.put("if-match", "");
		param.put("if-match", "");
	}

	public FrameRequestWrapper(HttpServletRequest request) {
		super(request);

	}

	/**
	 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
	 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
	 */
	@Override
	public String getParameter(String name) {
		if (StringsUtils.isEmpty(name)) {
			return "";
		}
		String value = super.getParameter(StringsUtils.doXssEncode(name));
		if (value != null) {
			value = StringsUtils.doXssEncode(value);
		}
		return value;
	}

	/**
	 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
	 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
	 * getHeaderNames 也可能需要覆盖
	 */
	@Override
	public String getHeader(String name) {
		if (StringsUtils.isEmpty(name)) {
			return "";
		}
		if (isSystemHead(name)) {
			return super.getHeader(name);
		}
		String value = super.getHeader(StringsUtils.doXssEncode(name));
		if (value != null) {
			value = StringsUtils.doXssEncode(value);
			boolean ret = false;
			try {
				ret = SecurityUtil.doSqlValidate(value, false);
			} catch (Throwable e1) {
				log.error("", e1);
			}
			if (ret){
				String msg = "";
				try {
					Map<String, String> m = new HashMap<String, String>();
					m.put("param", value);
					msg = FrameUtils.getWords("3000000000000024", "", m);
				} catch (Throwable e) {
					log.debug("", e);
				}
				throw new RuntimeException(msg);
			} else {
				return value;
			}
		}

		return "";
	}

	public String getParameterFromParent(String name) {
		String value = super.getParameter(StringsUtils.doXssEncode(name));

		return value;
	}
	private boolean isSystemHead(String name) {
		if (param.containsKey(name.toLowerCase())) {
			return true;
		}
		return false;
	}
}