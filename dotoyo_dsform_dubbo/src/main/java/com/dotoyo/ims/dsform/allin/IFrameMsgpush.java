package com.dotoyo.ims.dsform.allin;

import java.util.concurrent.BlockingQueue;

import net.sf.json.JSONArray;

/**
 * 消息推送模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameMsgpush extends IFrame {

	/**
	 * 取得当前用户最多10条的可用的消息
	 * 
	 * @param user
	 * @return
	 */
	public JSONArray getMsg4Wait(UserLoginBo user) throws Exception;

	public void messagePush(String userCode, String msg) throws Exception;

	public BlockingQueue<String> login(String userCode) throws Exception;

	public void logout(String userCode) throws Exception;

}
