package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.dotoyo.dsform.util.StringsUtils;

import net.sf.json.JSONObject;

public class UserSsoAuthorityIsLogin extends AbstractAuthorityIsLogin {
	public UserSsoAuthorityIsLogin(Map<String, String> param) {
		super(param);

	}

	public boolean isLogin(String userCode) throws Exception {

		// 调用老系统的 单点登录接口

		IFrameSession sessionFactory = FrameFactory.getSessionFactory(null);

		// 未登录,从cookie中获取sessionId
		String cookieName = "SSOSESSIONID";
		Cookie[] cookies = sessionFactory.getHttpRequest().getCookies();
		String sessionId = "";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					sessionId = cookie.getValue();
					if (!StringsUtils.isEmpty(sessionId)) {
						return isLoginBySso(sessionId);
					}
				}
			}

			// cookie中取不到，从parameter中取
			sessionId = (String) sessionFactory.getHttpRequest().getParameter(
					"sessionId");
			if (!StringsUtils.isEmpty(sessionId)) {
				return isLoginBySso(sessionId);
			}
			// 从共域中取
			HttpSession session = sessionFactory.getHttpSession();
			if (null != session) {
				sessionId = session.getId();
				return isLoginBySso(sessionId);
			}
		}

		return false;

	}

	/**
	 * 传入seesionId远程到其他系统，实现单点登录
	 * 
	 * @param parentSessionId
	 * @return
	 * @throws Exception
	 */
	protected boolean isLoginBySso(String parentSessionId) throws Exception {

		IFrameHttp http = FrameFactory.getHttpFactory(null);

		HttpBo bo = new HttpBo();
		Map<String, String> head = new HashMap<String, String>();
		//如果这个值为空，说明不需要远程单点登录
		String url = param.get("ssoUrl");
		//if(StringsUtils.isEmpty(url)){
		//	return false;
	//	}
		// String url = "http://localhost:7777/v10/user_sso_login";//
		

		bo.setUrl(url);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("sessionId", parentSessionId);
		// String json = "{sessionId:'" + sessionId + "'}";

		try {
			String json = http.postJson(bo, head, jsonObj.toString());
			loginByJson(parentSessionId, json);
			return true;
		} catch (Throwable e) {
			log.error("", e);
		}
		return false;
	}

	/**
	 * 
	 * @param json
	 * @throws Exception
	 */
	protected void loginByJson(String parentSessionId, String json)
			throws Exception {

		JSONObject jsonData = JSONObject.fromObject(json);
		String code = jsonData.getString("code");
		// 成功
		if (null != code && code.startsWith("0")) {
			JSONObject body = jsonData.getJSONObject("body");

			UserLoginBo user = new UserLoginBo();
			user.setUserCode(body.getString("usercode"));
			user.setUserName(body.getString("username"));
			user.setIp(body.getString("ip"));

			IFrameSession sf = FrameFactory.getSessionFactory(null);
			sf.saveUserLoginBo(parentSessionId,user);

		} else {
			String msg = jsonData.getString("msg");
			throw new Exception(msg);
		}

	}
}
