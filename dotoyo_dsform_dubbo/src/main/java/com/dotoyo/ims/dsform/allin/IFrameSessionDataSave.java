package com.dotoyo.ims.dsform.allin;

import java.util.Map;

public interface IFrameSessionDataSave {
	/**
	 * 从缓存中取用户
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SessionData getLoginUser(Map<String, String> param) throws Exception;

	/**
	 * 设置用户到缓存
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void setUserLogin(SessionData bo) throws Exception;

	/**
	 * 会话监听到结束时，调用这个方法清理会话缓存
	 * 
	 * @throws Exception
	 */
	public void setUserLogout(String sessionId) throws Exception;

	/**
	 * 从缓存中取用户
	 * @deprecated
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public SessionData getLoginSsoUser(String parentSessionId) throws Exception;

	
	/**
	 * @deprecated
	 * @param parentSessionId
	 * @throws Exception
	 */
	public void logoutSsoUser(String parentSessionId) throws Exception;

	/**
	 * 用于单点登录
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public void loginSsoUser(String parentSessionId, SessionData bo)
			throws Exception;

	/**
	 * 根据单点登录会话编号取当前会话编号
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public String getSessionId(String parentSessionId) throws Exception;

	public SessionData loginUser4back(String parentSessionId) throws Exception;
}
