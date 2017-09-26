 package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class FrameAuthority extends AbstractFrame implements IFrameAuthority {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameAuthority.class));

	protected static FrameAuthority instance = null;

	protected FrameAuthority() {

	}

	public static FrameAuthority getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameAuthority();
		}
	}

	@Override
	public void startup() {

	}

	@Override
	public void shutdown() {
		instance = null;

	}

	public Map<String, Object> userLogin(Map<String, Object> param)
			throws Exception {
		String userCode = param.get("userName") + "";
		String userPasswd = param.get("userPasswd") + "";
		String p = MD5JAVA.passwd4md5(userPasswd);
		IUserLogin bean = null;
		String type = param.get("type") + "";
		if ("admin".equals(userCode) && !type.equals("2")) {
			bean = new UserLogin4AdminBean();
		}else if ("test".equals(userCode)) {
			bean = new UserLogin4TestBean();
		} else {
			
			if ("1".equals(type)) {
				//登录跳核心平台
				bean = new UserLoginFromWebBean();

			} else if("2".equals(type)) {
				//协会登陆跳转
			    bean = new XieHuiUserLoginFromBean();
			}else if("3".equals(type)) {
				//企业登陆跳转
			    bean = new CpyUserLoginFromBean();
			}

		}
		if (bean != null) {
			return bean.userLogin(userCode, p, param);
		}
		return null;
	}

	public Map<String, String> userLogout(Map<String, String> param)
			throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(null);

		HttpSession s = sf.getHttpSession();

		if (s == null) {
			return null;
		} else {
			s.invalidate();

			Map<String, String> ret = new HashMap<String, String>();
			ret.put("loginUrl", AuthorityFilter.getLoginUrl());
			return ret;

		}
	}

	/**
	 * 用户登出
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> userLogout4Sso(Map<String, String> param)
			throws Exception {

		String parentSessionId = param.get("parentSessionId");
		if (StringsUtils.isEmpty(parentSessionId)) {
			Map<String, String> map = new HashMap<String, String>();

			FrameException e = new FrameException("3000000000000011", map);
			throw e;
		}
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		String sId = sf.getSessionId(parentSessionId);
		if (StringsUtils.isEmpty(sId)) {
			Map<String, String> map = new HashMap<String, String>();

			FrameException e = new FrameException("3000000000000011", map);
			throw e;
		}

		HttpSession s = null;

		s = sf.getHttpSession(sId);

		if (s == null) {
			return null;
		} else {
			s.invalidate();

			Map<String, String> ret = new HashMap<String, String>();
			ret.put("loginUrl", AuthorityFilter.getLoginUrl());
			return ret;

		}
	}

	public boolean isLogin(Map<String, String> param) throws Exception {
		String userCode = getLoginUserCode(param);
		if (!StringsUtils.isEmpty(userCode)) {
			return true;
		}
		IFrameStaticData staticFactory = FrameFactory
				.getStaticDataFactory(null);
		String url = staticFactory.getStaticData("app", "sso_url");
		IAuthorityIsLogin isLogin = null;

		if (StringsUtils.isEmpty(url)) {
			char pType = FrameFactory.getApplyProductType();

			switch (pType) {

			case '2': {
				isLogin = new AuthorityIsLogin4Cluster(param);
				break;
			}
			case '4': {
				FrameException e = new FrameException("3000000000000006",
						new HashMap<String, String>());
				throw e;

			}
			default: {
				isLogin = new BaseAuthorityIsLogin(param);
			}
			}
			return isLogin.isLogin(userCode);
		} else {
			param.put("ssoUrl", url);
			char pType = FrameFactory.getApplyProductType();

			switch (pType) {
			case '0': {
				//isLogin = new AppSsoAuthorityIsLogin(param);
				//break;
			}
			case '2': {
				isLogin = new UserSsoAuthorityIsLogin4Cluster(param);
				break;
			}
			case '4': {
				FrameException e = new FrameException("3000000000000006",
						new HashMap<String, String>());
				throw e;

			}
			default: {
				isLogin = new UserSsoAuthorityIsLogin(param);
			}
			}
			return isLogin.isLogin(userCode);
		}

	}

	/**
	 * @deprecated
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected boolean isLogin1(Map<String, String> param) throws Exception {
		String userCode = getLoginUserCode(param);
		if (!StringsUtils.isEmpty(userCode)) {
			return true;
		} else {
			IFrameSession sf = FrameFactory.getSessionFactory(param);
			HttpSession s = sf.getHttpSession();
			if (s == null) {
				return false;
			} else {

				UserLoginBo uBo = sf.loginUser4back(s.getId());
				if (uBo != null) {

					return true;
				} else {
					return false;
				}

			}
		}
	}

	/**
	 * 取登录用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo getLoginUser(Map<String, String> param) throws Exception {
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		return sf.getUserLoginBo();
	}

	/**
	 * 根据参数取用户名称
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserName(Map<String, String> param) throws Exception {
		UserLoginBo userMap = getLoginUser(param);

		if (userMap == null) {
			return "";
		} else {
			String userName = userMap.getUserName();
			if (StringsUtils.isEmpty(userName)) {
				return "";
			} else {
				return userName;
			}

		}
	}

	/**
	 * 根据参数取用户id
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserId(Map<String, String> param) throws Exception {
		UserLoginBo userMap = getLoginUser(param);

		if (userMap == null) {
			return "";
		} else {
			String userId = userMap.getUserId();
			if (StringsUtils.isEmpty(userId)) {
				return "";
			} else {
				return userId;
			}

		}
	}

	/**
	 * 根据参数取用户ip
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserIp(Map<String, String> param) throws Exception {
		UserLoginBo userMap = getLoginUser(param);

		if (userMap == null) {
			return "";
		} else {
			String ip = userMap.getIp();
			if (StringsUtils.isEmpty(ip)) {
				return "";
			} else {
				return ip;
			}

		}
	}

	public String getLoginUserCode(Map<String, String> param) throws Exception {
		UserLoginBo bo = getLoginUser(param);
		if (bo != null) {
			return bo.getUserCode();
		} else {
			return "";
		}
	}

	/**
	 * 取得登录的URL地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLoginUrl() throws Exception {
		return AuthorityFilter.getLoginUrl();
	}

	/**
	 * 取得根URL地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRootUrl() throws Exception {
		return AuthorityFilter.getRootUrl();
	}

	/**
	 * 用户登录
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void userLogin(UserLoginBo bo) throws Exception {
		// 处理用户当前中心
		bo.setCenter("1");
		//
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		sf.saveUserLoginBo("", bo);

	}

	public static void main(String[] args) throws Exception {
		String text = "1";
		FrameAuthority a = new FrameAuthority();
		String p = MD5JAVA.passwd4md5(text);
		log.debug(p);
	}

}
