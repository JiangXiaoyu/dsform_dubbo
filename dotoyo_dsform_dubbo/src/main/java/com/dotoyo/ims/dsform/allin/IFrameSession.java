package com.dotoyo.ims.dsform.allin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface IFrameSession {
	public HttpSession getHttpSession() throws Exception;

	public HttpServletRequest getHttpRequest() throws Exception;

	public HttpServletResponse getHttpResponse() throws Exception;

	public HttpSession getHttpSession(String sessionId) throws Exception;

	public String getHttpSessionId() throws Exception;

	/**
	 * 把用户信息保存到会话中
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void saveUserLoginBo(String parentSessionId,UserLoginBo bo) throws Exception;

	/**
	 * 从会话中取用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo getUserLoginBo() throws Exception;

	/**
	 * 直接取用户信息
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo getUserLoginBo(String sessionId) throws Exception;

	/**
	 * 用于后台重新登录:根据前端的会话编号取用户信息并登录
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public UserLoginBo loginUser4back(String parentSessionId) throws Exception;

	public String getSessionId(String parentSessionId) throws Exception;
	
	public static final  String SESSION_DATA="sessionData";
}
