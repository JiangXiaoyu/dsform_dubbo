package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

public interface IFrameKeyvalue {

	/**
	 * 取有且只有一条记录配置
	 * 
	 * @param module
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getKeyValue(String module, String code) throws Exception;

	/**
	 * 取有且只有一条记录配置
	 * 
	 * @param module
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getKeyValues(String module, String code)
			throws Exception;

	/**
	 * 取多条记录
	 * 
	 * @param module
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getKeyValueList(String module, String key)
			throws Exception;
}
