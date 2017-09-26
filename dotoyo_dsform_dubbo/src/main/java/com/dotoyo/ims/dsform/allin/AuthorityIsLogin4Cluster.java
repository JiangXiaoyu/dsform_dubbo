package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class AuthorityIsLogin4Cluster extends AbstractAuthorityIsLogin {
	public AuthorityIsLogin4Cluster(Map<String, String> param) {
		super(param);

	}

	public boolean isLogin(String userCode) throws Exception {

		// 调用老系统的 单点登录接口

		IFrameSession sessionFactory = FrameFactory.getSessionFactory(null);
		HttpServletRequest request = sessionFactory.getHttpRequest();

		String sessionId = (String) request.getHeader("sessionId");
		if (null != sessionId) {

			return isLogin4Cluster4Login(sessionId, param);
		} else {
			sessionId = (String) request.getParameter("sessionId");
			if (null != sessionId) {

				return isLogin4Cluster4Login(sessionId, param);
			}
		}

		return false;

	}

	private boolean isLogin4Cluster4Login(String sessionId,
			Map<String, String> param) throws Exception {
		IFrameSession sessionFactory = FrameFactory.getSessionFactory(null);

		UserLoginBo bo = sessionFactory.loginUser4back(sessionId);
		if (bo == null) {
			return false;
		}

		return true;
	}
}
