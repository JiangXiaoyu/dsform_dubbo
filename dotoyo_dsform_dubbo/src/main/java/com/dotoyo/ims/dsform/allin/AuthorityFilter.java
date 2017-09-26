package com.dotoyo.ims.dsform.allin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

import net.sf.json.JSONObject;

/**
 * 权限过滤器：未登录的用户访问需要登录的页面时，自动重定向到登录页面
 * 
 * @author xieshh
 * 
 */
public class AuthorityFilter implements Filter {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AuthorityFilter.class));
	private static List<String> urlList = null;
	private static String loginUrl = null;
	private static String rootUrl = null;

	public static String getLoginUrl() {
		return loginUrl;
	}

	public static String getRootUrl() {
		return rootUrl;
	}

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String curUrl = req.getRequestURI();
		char pType;
		try {
			pType = FrameFactory.getApplyProductType();

			switch (pType) {
			case '2': {
				doFilter4Local(request, response, chain);
				return;
			}
			case '4': {
				FrameException e = new FrameException("3000000000000006",
						new HashMap<String, String>());
				throw e;

			}
			default: {
				doFilter4Local(request, response, chain);
				return;
			}
			}
		} catch (FrameException e) {
			if (e.getCode().startsWith("0")) {
				// 成功了，直接返回
				return;
			}
			log.error("", e);
			try {
				IFrameAction aF = FrameFactory.getActionFactory(null);
				String curPath = req.getContextPath();
				curUrl = curUrl
						.substring(curPath.length() + 1, curUrl.length());
				if (aF.isBusiAction(curUrl)) {
					res.setCharacterEncoding("UTF-8");
					WebUtils.reportDataResponseError(res, e);
				} else {
					request.setAttribute("msg", e.getMessage());
					request.getRequestDispatcher("frame/error/forbidError.jsp")
							.forward(request, response);
				}
			} catch (Throwable e1) {
				log.error("", e1);
			}

		} catch (Throwable e) {
			log.error("", e);
			request.setAttribute("msg", e.getMessage() + "");
			request.getRequestDispatcher("/frame/error/error.jsp").forward(
					request, response);
		}
	}

	protected void doFilter4Local(ServletRequest request,
			ServletResponse response, FilterChain chain) throws Exception {
		// try {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String curUrl = WebUtils.getVisitUrl(httpRequest);
		// 资源不需要登录
		if (WebUtils.isResource(curUrl)) {

			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		// 配置的URL不需要登录
		if (inUrlList(curUrl)) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}

		// 验证通过的不需要登录
		if (isLogin(httpRequest, curUrl)) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		// 未登录的消息推送
		if ("/msgpush".equals(curUrl)) {
			return;
		}
		// 未登录的心跳
		if ("/heartbeat".equals(curUrl)) {
			return;
		}
		// 业务请求
		IFrameAction af = FrameFactory.getActionFactory(null);
		if (af.isBusiAction(curUrl)) {
			doFilter4Busi(httpRequest, httpResponse, chain);
			return;
		}
		// 跨域单点登录
		if (!login4Sso(httpRequest, httpResponse)) {
			// 重定向到登录页面
			httpResponse.sendRedirect(loginUrl);
		}

		// } catch (FrameException e) {
		// if (e.getCode().startsWith("0")) {
		// //成功了，直接返回
		// return;
		// }
		// } catch (Throwable e) {
		// log.error("", e);
		// request.setAttribute("msg", e.getMessage() + "");
		// request.getRequestDispatcher("/frame/error/error.jsp").forward(
		// request, response);
		// }
	}

	/**
	 * 以报文的形式通知业务操作请求非法：未登录
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doFilter4Busi(ServletRequest request,
			ServletResponse response, FilterChain chain) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		httpRequest.setCharacterEncoding(FrameBean.getInstance().getConfig(
				"encoding"));
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding(FrameBean.getInstance().getConfig(
				"encoding"));
		try {
			JSONObject body = new JSONObject();
			body.put("loginUrl", loginUrl);
			WebUtils.responseJsonError((HttpServletResponse) response,
					new FrameException("3000000000000011",
							new HashMap<String, String>()), body);
		} catch (Throwable e) {
			log.error("", e);
		}
	}

	protected boolean isLogin(HttpServletRequest httpRequest, String curlUrl)
			throws Exception {
		IFrameAuthority aF = FrameFactory.getAuthorityFactory(null);
		Map<String, String> param = new HashMap<String, String>();
		param.put("CURL_URL", curlUrl);
		HttpSession s = httpRequest.getSession(false);
		if (s == null) {
			param.put("SESSION_ID", "");
			if (aF.isLogin(param)) {

				return true;
			}
		} else {
			param.put("SESSION_ID", s.getId());
			if (aF.isLogin(param)) {

				return true;
			}
		}
		return false;
	}

	protected boolean inUrlList(String curUrl) throws IOException,
			ServletException {

		for (int i = 0; i < urlList.size(); i++) {
			String url = urlList.get(i);
			if (StringsUtils.isEmpty(url)) {
				continue;
			}
			if (url.endsWith("*")) {
				url = url.substring(0, url.length() - 1);
				if (!url.equals("/")) {
					if (url.endsWith("/")) {
						url = url.substring(0, url.length() - 1);
					}
				}
				if (curUrl.startsWith(url)) {

					return true;
				}
			} else {
				if (curUrl.equals(url)) {
					return true;
				}
			}
		}
		return false;

	}

	//
	// /**
	// * 取访问URL地址
	// *
	// * @param httpRequest
	// * @return
	// * @throws Exception
	// */
	// private String getVisitUrl(HttpServletRequest httpRequest) throws
	// Exception {
	//
	// String curUrl = WebUtils.getVisitUrl4NoRoot(httpRequest);
	// int id = curUrl.indexOf(';');
	// if (id != -1) {
	// curUrl = curUrl.substring(0, id);
	// }
	// id = curUrl.indexOf(':');
	// if (id != -1) {
	// curUrl = curUrl.substring(0, id);
	// }
	// id = curUrl.indexOf('?');
	// if (id != -1) {
	// curUrl = curUrl.substring(0, id);
	// }
	// return curUrl;
	// }

	public void init(FilterConfig config) throws ServletException {
		try {
			init4UrlList(config);
			init4LoginUrl(config);
			init4RootUrl(config);
			String msg = FrameUtils.getWords("3000000000000020",
					"权限模块过滤器开始加载......", null);
			log.debug(msg);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void init4UrlList(FilterConfig config) throws Exception {
		if (urlList == null) {
			synchronized (AuthorityFilter.class) {
				String urlListStr = config.getInitParameter("urlList");
				if (StringsUtils.isEmpty(urlListStr)) {
					urlListStr = "/*";
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", urlListStr);
					String msg = FrameUtils.getWords("2000000000000066",
							"使用到了系统的默认值${value}", map);
					log.debug(msg);
				}
				// 初始化URL过滤参数
				if (urlList == null) {
					List<String> urlList = StringsUtils.split(urlListStr, ',');
					AuthorityFilter.urlList = new Vector<String>(urlList);
				}
			}
		}

	}

	private void init4LoginUrl(FilterConfig config) throws Exception {
		if (AuthorityFilter.loginUrl == null) {
			AuthorityFilter.loginUrl = config.getInitParameter("loginUrl");
			if (StringsUtils.isEmpty(loginUrl)) {
				AuthorityFilter.loginUrl = "http://localhost/btframe/user_login";
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", AuthorityFilter.loginUrl);
				String msg = FrameUtils.getWords("2000000000000066",
						"使用到了系统的默认值${value}", map);
				log.debug(msg);
			}
		}

	}

	private void init4RootUrl(FilterConfig config) throws Exception {
		if (AuthorityFilter.rootUrl == null) {
			AuthorityFilter.rootUrl = config.getInitParameter("rootUrl");
			if (StringsUtils.isEmpty(AuthorityFilter.rootUrl)) {
				AuthorityFilter.rootUrl = "http://localhost/btframe";
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", AuthorityFilter.rootUrl);
				String msg = FrameUtils.getWords("2000000000000066",
						"使用到了系统的默认值${value}", map);
				log.debug(msg);
			}
		}
	}

	private boolean login4Sso(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws Exception {
		IFrameStaticData dataSv = FrameFactory.getStaticDataFactory(null);
		String backUrl = dataSv.getStaticData("web", "backUrl");

		if (StringsUtils.isEmpty(backUrl)) {
			return false;
		}
		userSsoRedirect(httpRequest, httpResponse, backUrl);
		return true;

	}

	private void userSsoRedirect(HttpServletRequest request,
			HttpServletResponse response, String backUrl) throws Exception {
		String queryString = request.getQueryString();
		String url = request.getRequestURL().toString();
		if (!url.equals(backUrl)) {
			if (!StringsUtils.isEmpty(queryString)) {
				url = String.format("%s?%s", new Object[] { url, queryString });
			}
			WebUtils.addCookie(response, "url", url, "/", 0);
		}
		//
		IFrameStaticData dataSv = FrameFactory.getStaticDataFactory(null);
		String ssoUrl = dataSv.getStaticData("app", "sso_url");
		String redUrl = String.format("%s?url=%s", new Object[] { ssoUrl,
				WebUtils.urlEncode(backUrl) });

		log.debug(redUrl);
		response.sendRedirect(redUrl);
	}
}
