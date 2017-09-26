package com.dotoyo.ims.dsform.allin;

/**
 * 缓存键接口类
 * 
 * @author xieshh
 * 
 */
public interface IKeyCache {

	/**
	 * 取缓存键
	 * 
	 * @param daoClsName
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getCacheKey(String daoClsName, Object key) throws Exception;

}
