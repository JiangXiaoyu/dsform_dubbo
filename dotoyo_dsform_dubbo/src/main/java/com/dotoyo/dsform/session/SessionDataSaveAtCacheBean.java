package com.dotoyo.dsform.session;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractSessionDataSaveBean;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameCache;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSession;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.SessionData;

/**
 * 用户许可存在缓存中
 * 
 * @author xieshh
 * 
 */
public class SessionDataSaveAtCacheBean extends AbstractSessionDataSaveBean {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SessionDataSaveAtCacheBean.class));

	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SessionData getLoginUser(Map<String, String> param) throws Exception {
		if (param != null) {
			String sessionId = param.get("sessionId");
			if (!StringsUtils.isEmpty(sessionId)) {
				return getLoginUser4Session(param, sessionId);
			}
		}
		return getLoginUser4Local(param);
	}

	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected SessionData getLoginUser4Local(Map<String, String> param)
			throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(param);
		HttpSession s = sf.getHttpSession();
		if (s == null) {
			// 未登录
			return null;
		} else {
			// String sId = s.getId();
			return getLoginUser4SessionId(s, param);

		}
	}

	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected SessionData getLoginUser4SessionId(HttpSession s,
			Map<String, String> param) throws Exception {

		String userCode = (String) s.getAttribute("USER_CODE");
		if (userCode == null) {
			// 未登录
			return null;
		}
		// 处理一级缓存
		long before = s.getAttribute("getUserData") == null ? 0 : (Long) s
				.getAttribute("getUserData");
		long now = System.currentTimeMillis();
		if (now - before < 2000) {
			SessionData data = (SessionData) s.getAttribute("USER");
			if (data != null) {
				return data;
			}
		}
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		String sId = s.getId();
		SessionData obj = (SessionData) cache.getCache(IFrameSession.SESSION_DATA, sId);
		try{
			s.setAttribute("USER", obj);
			s.setAttribute("getUserData", now);
		}catch (Throwable e) {
			
		}
		return obj;

	}

	protected SessionData getLoginUser4Session(Map<String, String> param,
			String sessionId) throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		HttpSession s = sf.getHttpSession(sessionId);
		if (s == null) {
			// 直接取
			IFrameCache cache = FrameFactory.getCacheFactory(null);
			SessionData obj = (SessionData) cache.getCache(IFrameSession.SESSION_DATA,
					sessionId);
			return obj;
		}
		// 从缓存中取
		return getLoginUser4SessionId(s, param);
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
		HttpSession s = req.getSession();
		if (s == null) {
			throw new FrameException("", null);
		}
		loginUser4Session(bo, s);

	}

	public void setUserLogout(String sId) throws Exception {

		IFrameCache cache = FrameFactory.getCacheFactory(null);
		SessionData bo = (SessionData) cache.getCache(IFrameSession.SESSION_DATA, sId);
		if (bo != null) {
			if (!StringsUtils.isEmpty(bo.getParentSessionId())) {
				cache.removeCache("parentSessionRefLocal", sId);
			}
			cache.removeCache(IFrameSession.SESSION_DATA, sId);

		}

	}

	public SessionData loginUser4back(String parentSessionId) throws Exception {
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		SessionData obj = (SessionData) cache.getCache(IFrameSession.SESSION_DATA,
				parentSessionId);
		if (obj == null) {
			return null;
		}
		obj.setParentSessionId(parentSessionId);
		setUserLogin(obj);
		cache.addCache("parentSessionRefLocal", parentSessionId,
				obj.getSessionId(), 60 * 60);
		return obj;
	}

	public String getSessionId(String parentSessionId) throws Exception {
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		String sId = (String) cache.getCache("parentSessionRefLocal",
				parentSessionId);
		return sId;
	}

	public SessionData getLoginSsoUser(String parentSessionId) throws Exception {
		if (StringsUtils.isEmpty(parentSessionId)) {
			return null;
		}
		String sessionId = getSessionId(parentSessionId);
		if (StringsUtils.isEmpty(sessionId)) {
			return null;
		}
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		HttpSession s = sf.getHttpSession(sessionId);
		if (s == null) {
			// 直接取
			IFrameCache cache = FrameFactory.getCacheFactory(null);
			SessionData obj = (SessionData) cache.getCache(IFrameSession.SESSION_DATA,
					sessionId);
			return obj;
		}
		// 从缓存中取
		return getLoginUser4Session(s);
	}

	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected SessionData getLoginUser4Session(HttpSession s) throws Exception {

		String userCode = (String) s.getAttribute("USER_CODE");
		if (userCode == null) {
			// 未登录
			return null;
		}
		// 处理一级缓存
		long before = s.getAttribute("getUserData") == null ? 0 : (Long) s
				.getAttribute("getUserData");
		long now = System.currentTimeMillis();
		if (now - before < 2000) {
			SessionData data = (SessionData) s.getAttribute("USER");
			if (data != null) {
				return data;
			}
		}
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		String sId = s.getId();
		SessionData obj = (SessionData) cache.getCache(IFrameSession.SESSION_DATA, sId);
		try{
		s.setAttribute("USER", obj);
		s.setAttribute("getUserData", now);
		}catch (Throwable e) {
		}
		return obj;

	}

	/**
	 * 用于单点登录
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public void loginSsoUser(String parentSessionId, SessionData bo)
			throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		HttpServletRequest req = sf.getHttpRequest();
		HttpSession s = req.getSession(true);
		loginUser4Session(bo, s);
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		cache.addCache("parentSessionRefLocal", bo.getParentSessionId(),
				s.getId(), 60 * 60);

	}

	/**
	 * 设置用户到缓存
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected void loginUser4Session(SessionData bo, HttpSession s)
			throws Exception {

		String sId = s.getId();
		IFrameCache cache = FrameFactory.getCacheFactory(null);
		bo.setSessionId(sId);
		cache.addCache(IFrameSession.SESSION_DATA, sId, bo, 60 * 60);
		s.setAttribute("USER_CODE", bo.getUserLogingBo().getUserCode());

	}

	public void logoutSsoUser(String parentSessionId) throws Exception {
		if (StringsUtils.isEmpty(parentSessionId)) {
			return;
		}
		String sessionId = getSessionId(parentSessionId);
		if (StringsUtils.isEmpty(sessionId)) {
			return;
		}
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		HttpSession s = sf.getHttpSession(sessionId);
		if (s == null) {

			return;
		}
		userLogout4Session(s);
	}

	protected void userLogout4Session(HttpSession s) throws Exception {

		if (s != null) {
			String sId = s.getId();
			IFrameCache cache = FrameFactory.getCacheFactory(null);
			SessionData bo = (SessionData) cache.getCache(IFrameSession.SESSION_DATA, sId);
			if (bo != null) {
				if (!StringsUtils.isEmpty(bo.getParentSessionId())) {
					cache.removeCache("parentSessionRefLocal", sId);
				}
				cache.removeCache(IFrameSession.SESSION_DATA, sId);

			}
		}
	}

}
