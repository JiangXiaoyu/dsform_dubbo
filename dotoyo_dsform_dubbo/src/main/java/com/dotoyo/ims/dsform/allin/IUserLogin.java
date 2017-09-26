package com.dotoyo.ims.dsform.allin;

import java.util.Map;

public interface IUserLogin {
	
	/**
	 * 用户登录接口
	 * @param userCode
	 * @param userPasswd
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> userLogin(String userCode,String userPasswd,
			Map<String, Object> param) throws Exception;
}
