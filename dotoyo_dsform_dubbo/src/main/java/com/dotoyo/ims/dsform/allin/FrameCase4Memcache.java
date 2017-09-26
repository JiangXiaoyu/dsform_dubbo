package com.dotoyo.ims.dsform.allin;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.cache.Cache;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 缓存实现：用于多节点缓存：未完成
 * 
 * @author xieshh
 * 
 */
public class FrameCase4Memcache implements Cache {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameCase4Memcache.class));
	private String id = "";
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public String getId() {

		return id;
	}

	public int getSize() {
		IFrameCache c;
		try {
			c = FrameCache4Memcached4More.getInstance(null);
			return c.getSize(id);
		} catch (Throwable e) {
			log.error("", e);
			throw new RuntimeException("getSize");
		}

	}

	public void putObject(Object key, Object value) {
		IFrameCache c;
		try {
			c = FrameCache4Memcached4More.getInstance(null);
			c.addCache(id, key, value, 60*60);
		} catch (Throwable e) {
			log.error("", e);
			throw new RuntimeException("putObject");
		}

	}

	public Object getObject(Object key) {
		IFrameCache c;
		try {
			c = FrameCache4Memcached4More.getInstance(null);
			return c.getCache(id, key);
		} catch (Throwable e) {
			log.error("", e);
			throw new RuntimeException("getObject");
		}

	}

	public Object removeObject(Object key) {

		IFrameCache c;
		try {
			c = FrameCache4Memcached4More.getInstance(null);
			return c.getCache(id, key);
		} catch (Throwable e) {
			log.error("", e);
			throw new RuntimeException("getObject");
		}

	}

	public void clear() {

		IFrameCache c;
		try {
			c = FrameCache4Memcached4More.getInstance(null);
			c.clearCache(id);
		} catch (Throwable e) {
			log.error("", e);
			throw new RuntimeException("clear");
		}
	}

	public ReadWriteLock getReadWriteLock() {

		return readWriteLock;
	}

	public FrameCase4Memcache(String id) {
		this.id = id;
	}

}
