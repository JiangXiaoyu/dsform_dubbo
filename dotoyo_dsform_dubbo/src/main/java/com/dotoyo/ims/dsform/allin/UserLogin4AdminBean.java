package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class UserLogin4AdminBean implements IUserLogin {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(UserLogin4AdminBean.class));

	public Map<String, Object> userLogin(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception {

		String passwd = param.get("userPasswd") + "";
		String p = MD5JAVA.passwd4md5(passwd);
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITpUserSv sv = (ITpUserSv) svF
				.getService("com.dotoyo.admin.service.inter.ITpUserSv");
		TpUserModel userModel = loginUser(userCode, userPasswd, param);
		if (userModel == null) {
//			if (!"C4CA4238A0B923820dCC509A6F75849B".equals(p)) {
//				return null;
//			}
			return null;
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
		//
		user.put("mainFrameUrl", "/webPage_mainFrame?skin="
				+ WebUtils.getSkin4ligerui());
		return user;

	}
	
	public TpUserModel loginUser(String userCode, String userPasswd,
			Map<String, Object> param) throws Exception {
		IFrameService svF = FrameFactory.getServiceFactory(null);
		ITpAccountDao dao = (ITpAccountDao) svF
				.getService("com.dotoyo.admin.dao.inter.ITpAccountDao");
		param.put("code", userCode);
		param.put("passwd", userPasswd);
		param.put("state", "1");
		List<Map> rows = dao.selectList4Map(param);
		if (rows.size() > 0) {
			Map row = rows.get(0);
			TpAccountModel model = ClassUtils.map2Bean(row,
					TpAccountModel.class);
			TpUserModel userModel = new TpUserModel();
			userModel.setId(model.getUserId());
			return userModel;
		}

		return null;
	}

}
