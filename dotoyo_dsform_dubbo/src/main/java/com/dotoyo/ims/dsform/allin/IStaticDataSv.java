
package com.dotoyo.ims.dsform.allin;

import java.util.Map;

/**
 * 所查询模块相关服务，
 * 
 * @author Administrator
 * 
 */
public interface IStaticDataSv {

	public String saveStaticData(Map<String, String> map) throws Exception;

	public boolean deleteStaticData(Map<String, String> ma) throws Exception;

	public Map<String, String> searchStaticData(Map<String, String> map) throws Exception;

	public int updateStaticData(Map<String, String> map)
			throws Exception;

	public int enableStaticData(String id);

	public int disableStaticData(String id);
	
	public Map<String, Object> getStaticData(String module, String key)throws Exception;

}
