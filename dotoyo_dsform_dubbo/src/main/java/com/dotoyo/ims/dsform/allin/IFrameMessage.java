package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dotoyo.dsform.model.IModel;
import com.dotoyo.dsform.model.MessageBo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 报文模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameMessage {

	/**
	 * 把报文字串变为对象
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getReqMessage(MessageBo bo, String msg)
			throws Exception;

	/**
	 * 把报文字串变为对象
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getReqMessage(HttpServletRequest req)
			throws Exception;

	// ////////////////////////////////////////////////////////////////////////////
	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, Map<String, Object> param)
			throws Exception;
	public String getRespMessage(MessageBo bo)
	throws Exception;
	public String getRespMessage(MessageBo bo, IModel model)
	throws Exception;
	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo,JSONObject json,String code,String msg)
			throws Exception;
	
	


	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, PageListModel model)
			throws Exception;
	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, List<Map<String, String>> list,long total)
			throws Exception;
	
	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage(MessageBo bo, String msg, long count)
			throws Exception;
	/**
	 * 把对象变为报文字串
	 * 
	 * @param bo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public String getRespMessage4List(MessageBo bo, Throwable e)
			throws Exception;
	
	public String getRespMessage(MessageBo bo, Throwable e) throws Exception;


	public String getRespMessage(MessageBo bo,Throwable e,JSONObject json) throws Exception;
	
	public String getRespMessage(MessageBo bo, JSONArray param) throws Exception;
}
