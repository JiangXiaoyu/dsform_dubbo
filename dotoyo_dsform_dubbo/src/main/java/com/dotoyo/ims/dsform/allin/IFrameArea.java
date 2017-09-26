package com.dotoyo.ims.dsform.allin;

import java.util.List;

/**
 * 区域模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameArea {
	/**
	 * 根据区域编码取区域信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public AreaBean getAreaByCode(String code) throws Exception;

	/**
	 * 分页取区域列表
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<AreaBean> getAreaList(int start, int end) throws Exception;

	/**
	 * 分页取子区域列表
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<AreaBean> getAreaChildList(int start, int end, String areaCode)
			throws Exception;

	/**
	 * 分页取子及孙子区域列表
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<AreaBean> getAreaAllChildList(int start, int end,
			String areaCode) throws Exception;

}
