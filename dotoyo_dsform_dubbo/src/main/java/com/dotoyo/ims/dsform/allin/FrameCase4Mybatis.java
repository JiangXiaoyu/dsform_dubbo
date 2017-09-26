package com.dotoyo.ims.dsform.allin;

import java.util.concurrent.locks.ReadWriteLock;

import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.Cache;

import com.dotoyo.dsform.log.LogProxy;

public class FrameCase4Mybatis implements Cache {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCase4Mybatis.class));

	private Cache impl = null;

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

		return impl.removeObject(key);
	}

	public void clear() {
		impl.clear();
	}

	public ReadWriteLock getReadWriteLock() {

		return impl.getReadWriteLock();
	}

	public FrameCase4Mybatis(String id) {
		impl = new org.apache.ibatis.cache.impl.PerpetualCache(id);
	}
}
