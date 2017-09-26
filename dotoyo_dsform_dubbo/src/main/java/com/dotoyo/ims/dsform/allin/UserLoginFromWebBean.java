package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class UserLoginFromWebBean implements IUserLogin {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(UserLoginFromWebBean.class));

	public Map<String, Object> userLogin(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception {
		TpAccountModel model = new TpAccountModel();
		model.setCode(userCode);
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITpUserSv sv = (ITpUserSv) svF
				.getService("com.dotoyo.admin.service.inter.ITpUserSv");
		TpUserModel userModel = sv.loginUser(userCode, userPasswd, param);
		if (userModel==null) {
			return null;
		}
		//
		UserLoginBo bo = new UserLoginBo();
		bo.setIp((String)param.get("ip"));
		bo.setUserCode(userCode);
		bo.setUserId(userModel.getId());
		bo.setUserName(userModel.getName());
		bo.setCenter("1");

		//
		IFrameSession sf = FrameFactory.getSessionFactory(null);
		sf.saveUserLoginBo("", bo);


		Map<String, Object> user = new HashMap<String, Object>();
		user.put("code", bo.getUserCode());
		user.put("name", bo.getUserName());
		//
		user.put("mainFrameUrl", "/webPage_mainFrame?skin="
				+ WebUtils.getSkin4ligerui());
		return user;

	}

}
