package com.dotoyo.ims.dsform.allin;

/**
 * 处理用户的会话信息
 * 
 * @author xieshh
 * 
 */
public interface IAuthorityIsLogin {
	/**
	 * 根据用户编码判断用户是否已登录
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public boolean isLogin(String userCode) throws Exception;

}
