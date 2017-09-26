package com.dotoyo.ims.dsform.allin;

/**
 * 分为页面请求[无事务]与业务请求[原子事务]
 * 
 * @author xieshh
 * 
 */
public interface IFrameAction {

	/**
	 * 根据业务与操作，取结果配置
	 * 
	 * @param action
	 * @param method
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public String getActionResultByCode(String action, String method,
			String result) throws Exception;

	/**
	 * 根据URL判断是否是业务请求
	 * 
	 * @param curUrl
	 * @return
	 * @throws Exception
	 */
	public boolean isBusiAction(String curUrl) throws Exception;
}
