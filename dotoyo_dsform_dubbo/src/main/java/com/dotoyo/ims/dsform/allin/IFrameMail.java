package com.dotoyo.ims.dsform.allin;

import java.util.Map;

/**
 * 邮件模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameMail {

	/**
	 * 发邮件
	 * 
	 * @param param
	 * @throws Exception
	 */
	public void sendMain(Map<String, String> param) throws Exception;
}
