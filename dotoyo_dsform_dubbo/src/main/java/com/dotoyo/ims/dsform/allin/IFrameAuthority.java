package com.dotoyo.ims.dsform.allin;

import java.util.Map;

/**
 * 处理用户的会话信息
 * 
 * @author xieshh
 * 
 */
public interface IFrameAuthority {
	/**
	 * 是根据参数判断用户是否已登录
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean isLogin(Map<String, String> param) throws Exception;

	/**
	 * 用户登录
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> userLogin(Map<String, Object> param)
			throws Exception;
	
	/**
	 * 用户登录
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void userLogin(UserLoginBo bo)
			throws Exception;

	/**
	 * 用户登出
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> userLogout(Map<String, String> param)
			throws Exception;
	/**
	 * 用户登出
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> userLogout4Sso(Map<String, String> param)
			throws Exception;
	/**
	 * 取登录用户信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo getLoginUser(Map<String, String> param) throws Exception;

	/**
	 * 根据参数取用户名称
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserName(Map<String, String> param) throws Exception;

	/**
	 * 根据参数取用户id
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserId(Map<String, String> param) throws Exception;

	/**
	 * 根据参数取用户登录帐号
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserCode(Map<String, String> param) throws Exception;

	/**
	 * 根据参数取用户ip
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getLoginUserIp(Map<String, String> param) throws Exception;

	/**
	 * 取得登录的URL地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLoginUrl() throws Exception;

	/**
	 * 取得根URL地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRootUrl() throws Exception;

}
