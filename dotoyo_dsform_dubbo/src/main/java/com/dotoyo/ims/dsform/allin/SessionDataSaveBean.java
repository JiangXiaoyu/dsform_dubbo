package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 用户许可存在缓存中
 * 
 * @author xieshh
 * 
 */
public class SessionDataSaveBean extends AbstractSessionDataSaveBean {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SessionDataSaveBean.class));

	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SessionData getLoginUser(Map<String, String> param) throws Exception {

		return getLoginUser4Local(param);

	}

	protected SessionData getLoginUser4Local(Map<String, String> param)
			throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(param);
		HttpSession s = sf.getHttpSession();
		if (s == null) {
			return null;
		} else {

			SessionData obj = (SessionData) s.getAttribute("USER");
			return obj;

		}
	}

	/**
	 * 设置用户到缓存
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void setUserLogin(SessionData bo) throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		HttpServletRequest req = sf.getHttpRequest();
		HttpSession s = req.getSession(true);
		bo.setSessionId(s.getId());
		s.setAttribute("USER", bo);
		UserLoginBo uBo = bo.getUserLogingBo();
		if (uBo != null) {
			s.setAttribute("USER_CODE", uBo.getUserCode());
		}

	}

	public void setUserLogout(String sessionId) throws Exception {
		
	}

	public SessionData loginUser4back(String parentSessionId) throws Exception {
		throw new FrameException("3000000000000030", null);
	}

	public String getSessionId(String parentSessionId) throws Exception {
		throw new FrameException("3000000000000030", null);
	}

	public SessionData getLoginSsoUser(String parentSessionId) throws Exception {
		throw new FrameException("3000000000000030", null);
	}

	public void logoutSsoUser(String parentSessionId) throws Exception {
		throw new FrameException("3000000000000030", null);
		
	}

	public void loginSsoUser(String parentSessionId, SessionData bo)
			throws Exception {
		throw new FrameException("3000000000000030", null);
		
	}
}
