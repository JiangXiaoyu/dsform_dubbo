package com.dotoyo.ims.dsform.allin;


public interface IFrameCenter {
	
	/**
	 * 取当前中心
	 * @return
	 * @throws Exception
	 */
	public String getCurrentCenter() throws Exception;
	
	/**
	 * 设当前中心
	 * @return
	 * @throws Exception
	 */
	public void setCurrentCenter(String center) throws Exception;
	
	/**
	 * 取当前中心
	 * @return
	 * @throws Exception
	 */
	public String getCenterByBusiId(String busiId) throws Exception;
	
	/**
	 * 按月分表
	 * @return
	 * @throws Exception
	 */
	public String getTableSubNameByMonth() throws Exception;

}
