package com.dotoyo.ims.dsform.allin;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.Cache;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class FrameCache4Mybatis implements ICache {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCache4Mybatis.class));
	private static final Map<String, ICache> map = new ConcurrentHashMap<String, ICache>();

	private Cache impl = null;

	private boolean isMybatisCache = true;

	public String getId() {

		return impl.getId();
	}

	public int getSize() {

		return impl.getSize();
	}

	public void putObject(Object key, Object value) {

		impl.putObject(key, value);
	}

	public Object getObject(Object key) {

		return impl.getObject(key);
	}

	public Object removeObject(Object key) {

		throw new RuntimeException("no implement");
	}

	public void clear() {
		Set<String> set = null;
		try {
			set = CacheConfigBean.getInstance().getDaocls(getId());
		} catch (Throwable e) {
			log.error("", e);
		}
		if (set != null) {
			for (String daoCls : set) {
				ICache c = map.get(daoCls);
				if (c != null) {
					c.clear4NoCycle();
				}
			}
		}
		clear4NoCycle();
	}

	public void clear4NoCycle() {
		impl.clear();
	}

	public ReadWriteLock getReadWriteLock() {

		return impl.getReadWriteLock();
	}

	public FrameCache4Mybatis(String id) {

		try {
			String cacheImpl = FrameBean.getInstance().getConfig("cache");
			if (FrameCache4Memcached4More.class.getName().equals(cacheImpl)) {
				impl = new FrameCase4Memcache(id);
			} else {
				impl = new FrameCase4Mybatis(id);
			}
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException("FrameCache4Mybatis");
		}

		map.put(id, this);
	}

	public static ICache getCache(String obj) {
		if (StringsUtils.isEmpty(obj)) {
			throw new RuntimeException("param cant null");
		}
		ICache c = map.get(obj);
		if (c == null) {
			return newCache(obj);
		}
		return c;
	}
	public boolean isUserCache(){
		return !isMybatisCache;
	}
	synchronized private static ICache newCache(String obj) {
		ICache c = map.get(obj);
		if (c == null) {
			FrameCache4Mybatis c1 = new FrameCache4Mybatis(obj);
			c1.isMybatisCache = false;
			map.put(obj, c1);
			c = c1;
		}
		return c;
	}
}
