
package com.dotoyo.dsform.service.inter;

import com.dotoyo.dsform.model.SerialNum;
import com.dotoyo.ims.dsform.allin.SnEntity;

import net.sf.json.JSONObject;

public interface ISerialNumService {
	
	public void insert(SerialNum sn) throws Exception;
	
	public void update(SerialNum sn) throws Exception;
	
	/*
	 * 根据id查询流水号记录，并把这条记录锁住
	 */
	public SerialNum selectById4Lock(String id) throws Exception;

	/*
	 * 获得显示的流水号
	 */
	public JSONObject getShowSn(JSONObject reqJson)throws Exception;
	
	/*
	 * 获得保存的流水号
	 */
	public SnEntity lockSaveSn(JSONObject reqJson)throws Exception;

	public int saveSnCurValue(JSONObject reqJson)throws Exception ;

	public boolean isFirstUsePreform(JSONObject reqJson)throws Exception;
}
