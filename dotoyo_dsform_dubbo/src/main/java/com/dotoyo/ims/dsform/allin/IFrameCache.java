package com.dotoyo.ims.dsform.allin;

/**
 * 缓存模块：统一缓存管理
 * 
 * @author xieshh
 * 
 */
public interface IFrameCache {

	public int getSize(String daoClsName) throws Exception;

	/**
	 * 新增缓存
	 * 
	 * @param daoClsName
	 * @param key
	 * @param value
	 * @param time
	 *            单位为秒
	 * @throws Exception
	 */
	public void addCache(String daoClsName, Object key, Object value, int time)
			throws Exception;

	/**
	 * 取DAO层的缓存
	 * 
	 * @param daoClsName
	 * @throws Exception
	 */
	public Object getCache(String daoClsName, Object key) throws Exception;

	/**
	 * 清空指定DAO的缓存
	 * 
	 * @param daoClsName
	 * @throws Exception
	 */
	public Object removeCache(String daoClsName, Object key) throws Exception;

	/**
	 * 清空指定DAO的缓存：如果有其它节点，需要通知
	 * @param daoClsName
	 * @throws Exception
	 */
	public void clearCache(String daoClsName) throws Exception;
	
	/**
	 * 清空指定DAO的缓存不通知其他节点
	 * @param daoClsName
	 * @throws Exception
	 */
	public void clearCache4Inform(String daoClsName) throws Exception;

	/**
	 * 新增缓存:支持重复读
	 * 
	 * @param daoClsName
	 * @param key
	 * @param value
	 * @param time
	 *            单位为秒
	 * @throws Exception
	 */
	public boolean addCacheBo(String daoClsName, Object key, int time,
			CacheBo bo) throws Exception;

	/**
	 * 取DAO层的缓存:支持重复读
	 * 
	 * @param daoClsName
	 * @throws Exception
	 */
	public CacheBo getCacheBo(String daoClsName, Object key) throws Exception;

	/**
	 * 清空指定DAO的缓存:支持重复读
	 * 
	 * @param daoClsName
	 * @throws Exception
	 */
	public Object removeCacheBo(String daoClsName, Object key, CacheBo bo)
			throws Exception;
	/******************************/

}
