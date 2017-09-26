package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 缓存模块
 * 
 * @author xieshh
 * 
 */
public class FrameCache extends AbstractFrame implements IFrameCache {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCache.class));

	protected static FrameCache instance = null;

	protected FrameCache() {

	}

	public static FrameCache getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameCache();
		}
	}

	public void startup() {
		//FrameCache4Memcached1.getInstance(null).startup();
		log.debug("startup");
	}

	public void shutdown() {
		//FrameCache4Memcached1.getInstance(null).shutdown();
	}

	/**
	 */
	public void clearCache(String daoClsName) throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			c.clear();
			InformCacheModify.getInstance(null).infomCacheModify(daoClsName);
		}
	}
	

	public void clearCache4Inform(String daoClsName )
			throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			c.clear();
		}
	}

	public void addCache(String daoClsName, Object key, Object value, int time)
			throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			c.putObject(key, value);
		}

	}

	public Object getCache(String daoClsName, Object key)
			throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			return c.getObject(key);
		}
		return null;
	}

	public int getSize(String daoClsName) throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			return c.getSize();
		}
		return 0;
	}
	public Object removeCache(String daoClsName, Object key)
			throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			return c.removeObject(key);
		}
		return null;
	}
	public Object removeCacheBo(String daoClsName, Object key,CacheBo bo)
			throws Exception {
		ICache c = FrameCache4Mybatis.getCache(daoClsName);
		if (c != null) {
			return c.removeObject(key);
		}
		return null;
	}
	public static void main(String[] args) throws Exception {
		IFrameCache c = FrameFactory.getCacheFactory(null);
		String daoClsName = "com.dotoyo.frame.staticdata.dao.IStaticDataDao";
		Object key = "1";
		Object value = "a";
		int time = 1;
		c.addCache(daoClsName, key, value, time);
		Object ret = c.getCache(daoClsName, key);
		log.debug(ret);
	}

	public boolean addCacheBo(String daoClsName, Object key, int time,
			CacheBo bo) throws Exception {
		this.addCache(daoClsName, key, bo.getValue(), time);
		return true;
	}

	public CacheBo getCacheBo(String daoClsName, Object key)
			throws Exception {
		CacheBo bo = new CacheBo();
		Object obj = getCache(daoClsName, key);
		bo.setValue(obj);
		return bo;
	}

	
}
