package com.dotoyo.ims.dsform.allin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * 缓存按Map分组
 * 
 * @author xieshh
 * 
 */
public class MapValue4MemcacheBean1 {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MapValue4MemcacheBean1.class));

	protected static MapValue4MemcacheBean1 instance = null;

	protected MapValue4MemcacheBean1() {

	}

	private Map<String, Map<String, String>> types = new ConcurrentHashMap<String, Map<String, String>>();

	public static MapValue4MemcacheBean1 getInstance() {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new MapValue4MemcacheBean1();
		}
	}

	public void clearCache(MemcachedClient xmc, String daoClsName)
			throws Exception {

		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
		}
		for (String key : keys.keySet()) {
			try {
				xmc.delete(key, (long) 3000);
			} catch (Throwable e) {

			}
		}
		// 通知其它节点，清理缓存

	}

	public void addCache(MemcachedClient xmc, String daoClsName, Object key,
			Object value, int time) throws Exception {
		String keyStr = fixKey(daoClsName, key);
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
		}
		keys.put(keyStr, "");
		xmc.set(keyStr, time, value, 3000);
	}

	public Object getCache(MemcachedClient xmc, String daoClsName, Object key)
			throws Exception {

		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
			return null;
		}
		String keyStr = fixKey(daoClsName, key);
		if (!keys.containsKey(keyStr)) {
			return null;
		}

		Object obj = xmc.get(keyStr, 3000);
		if (obj == null) {
			return null;
		}
		return obj;
	}

	public int getSize(MemcachedClient xmc, String daoClsName) throws Exception {
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
		}
		return keys.size();
	}

	public Object removeCache(MemcachedClient xmc, String daoClsName, Object key)
			throws Exception {
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
			return null;
		}
		String keyStr = fixKey(daoClsName, key);
		if (!keys.containsKey(keyStr)) {
			return null;
		}
		return xmc.delete(keyStr, (long) 3000);
	}

	public Object removeCacheBo(MemcachedClient xmc, String daoClsName,
			Object key, CacheBo bo) throws Exception {
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
			return null;
		}
		String keyStr = fixKey(daoClsName, key);
		if (!keys.containsKey(keyStr)) {
			return null;
		}
		return xmc.delete(fixKey(daoClsName, key), bo.getCas(), 3000);

	}

	public boolean addCacheBo(MemcachedClient xmc, String daoClsName,
			Object key, int time, CacheBo bo) throws Exception {

		String keyStr = fixKey(daoClsName, key);
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
		}
		keys.put(keyStr, "");
		return xmc.cas(keyStr, 0, bo.getValue(), 3000, bo.getCas());

	}

	public CacheBo getCacheBo(MemcachedClient xmc, String daoClsName, Object key)
			throws Exception {
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = newType(daoClsName);
			return null;
		}
		String keyStr = fixKey(daoClsName, key);
		if (!keys.containsKey(keyStr)) {
			return null;
		}

		GetsResponse<Object> resp = xmc.gets(daoClsName, 3000);
		if (resp == null) {
			return null;
		}
		Object obj = resp.getValue();
		CacheBo bo = new CacheBo();
		bo.setCas(resp.getCas());
		bo.setValue(obj);
		return bo;

	}

	private String fixKey(String daoClsName, Object key) throws Exception {
		IKeyCache keyCache = new KeyCache4Memcache();
		return keyCache.getCacheKey(daoClsName, key);
	}

	synchronized private Map<String, String> newType(String daoClsName)
			throws Exception {
		Map<String, String> keys = types.get(daoClsName);
		if (keys == null) {
			keys = new ConcurrentHashMap<String, String>();
			types.put(daoClsName, keys);
		}
		return keys;
	}
}
