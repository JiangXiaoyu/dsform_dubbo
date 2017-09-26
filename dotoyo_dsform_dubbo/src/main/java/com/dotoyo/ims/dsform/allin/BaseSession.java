package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.service.BaseRequestListener;
import com.dotoyo.dsform.util.StringsUtils;

public class BaseSession extends AbstractFrame implements IFrameSession {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseSession.class));

	protected static BaseSession instance = null;

	protected BaseSession() {

	}

	public static BaseSession getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new BaseSession();
		}
	}

	@Override
	public void startup() {

	}

	@Override
	public void shutdown() {
		instance = null;

	}

	public HttpSession getHttpSession() throws Exception {
		HttpServletRequest request = getHttpRequest();
		if (request == null) {
			return null;
		}
		return request.getSession(false);
	}

	public HttpServletRequest getHttpRequest() throws Exception {
		HttpServletRequest request = BaseRequestListener.getServletRequest();
		return request;
	}

	public HttpServletResponse getHttpResponse() throws Exception {
		// 3000000000000006=功能未实现
		FrameException e = new FrameException("3000000000000006",
				new HashMap<String, String>());
		throw e;
	}

	public HttpSession getHttpSession(String sessionId) throws Exception {
		return FrameSessionListener.getSession().get(sessionId);
	}

	public String getHttpSessionId() throws Exception {
		HttpSession s = getHttpSession();
		if (s == null) {
			return null;
		}
		return s.getId();
	}

	public void saveUserLoginBo(String parentSessionId, UserLoginBo bo)
			throws Exception {
		IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
		SessionData sd = new SessionData("", bo);

		sd.setLastAccessTime(System.currentTimeMillis());
		sd.setLastSignTime(System.currentTimeMillis());
		if (StringsUtils.isEmpty(parentSessionId)) {
			save.setUserLogin(sd);
		} else {
			sd.setParentSessionId(parentSessionId);
			save.loginSsoUser(parentSessionId, sd);
		}
		//

	}

	public UserLoginBo getUserLoginBo() throws Exception {
		IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
		SessionData sd = save.getLoginUser(null);
		if (sd == null) {
			return null;
		}
		return sd.getUserLogingBo();
	}

	/**
	 * 从会话中取用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo getUserLoginBo(String sessionId) throws Exception {
		IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
		Map<String, String> map = new HashMap<String, String>();
		map.put("sessionId", sessionId);
		SessionData sd = save.getLoginUser(map);
		if (sd == null) {
			return null;
		}
		return sd.getUserLogingBo();
	}

	public UserLoginBo loginUser4back(String parentSessionId) throws Exception {
		IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
		SessionData sd = save.loginUser4back(parentSessionId);
		if (sd == null) {
			return null;
		}
		return sd.getUserLogingBo();
	}

	public String getSessionId(String parentSessionId) throws Exception {
		IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
		String sId = save.getSessionId(parentSessionId);

		return sId;
	}
}
