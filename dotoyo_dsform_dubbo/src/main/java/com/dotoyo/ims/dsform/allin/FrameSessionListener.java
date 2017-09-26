package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * @author Administrator
 * 
 */
public class FrameSessionListener implements HttpSessionListener {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameSessionListener.class));
	protected static final Map<String, HttpSession> session = new ConcurrentHashMap<String, HttpSession>(
			1024);

	public FrameSessionListener() {
		super();
	}

	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession s = arg0.getSession();
		s.setAttribute("LAST_ACCESS_UPDATE_TIME", System.currentTimeMillis());
		s.setAttribute("LAST_SIGN_UPDATE_TIME", System.currentTimeMillis());
		// 取用户信息
		session.put(s.getId(), s);
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession s = arg0.getSession();
		session.remove(s.getId());

		//
		try {
			char pType;

			pType = FrameFactory.getApplyProductType();

			switch (pType) {
			case '0': {
				sessionDestroyed4font(arg0, s);
				return;
			}
			case '1': {
				sessionDestroyed4font(arg0, s);
				return;
			}
			case '2': {
				sessionDestroyed4back(arg0, s);
				return;
			}
			case '4': {
				FrameException e = new FrameException("3000000000000006",
						new HashMap<String, String>());
				throw e;

			}
			default: {
				return;
			}
			}
		} catch (Throwable e) {
			log.error("", e);
		}
	}

	/**
	 * 前端清理缓存
	 * 
	 * @param arg0
	 * @param s
	 */
	private void sessionDestroyed4font(HttpSessionEvent arg0, HttpSession s) {

		// 通过getUserCode判断是否有用户登录并销毁sessionData
		try {
			
			IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
			UserLoginBo bo=sf.getLoginUser(null);
			if (bo != null) {
				IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
				save.setUserLogout(s.getId());
			}
			
			

		} catch (Throwable e) {
			log.error("", e);
		}
	}

	/**
	 * 前端清理缓存
	 * 
	 * @param arg0
	 * @param s
	 */
	private void sessionDestroyed4back(HttpSessionEvent arg0, HttpSession s) {

		// 通过getUserCode判断是否有用户登录并销毁sessionData
		try {
			
			IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
			UserLoginBo bo=sf.getLoginUser(null);
			if (bo != null) {
				IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
				save.setUserLogout(s.getId());
			}
			
			

		} catch (Throwable e) {
			log.error("", e);
		}
	}
	
	/**
	 * 用于对当前节点的会话进行清理
	 * 
	 * @return
	 */
	public static Map<String, HttpSession> getSession() {
		return session;
	}

}
