
package com.dotoyo.ims.dsform.allin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 当有会话请求时，将调用这个方法处理会话信息
 * 
 * @author xieshh
 * 
 */
public interface IFrameSessionRequest {
	public void doRequest(HttpServletRequest request, HttpSession session)
			throws Exception;

}
