package com.dotoyo.ims.dsform.allin;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class KeyCache4Memcache implements IKeyCache {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(KeyCache4Memcache.class));

	public String getCacheKey(String daoClsName, Object key) throws Exception {
		String keyStr = String.format("%s_%s", daoClsName, key.toString());
		int len=125;
		String midStr="hash";
		if (keyStr.length() > len) {
			String hash=String.valueOf(key.hashCode());
			keyStr = String.format("%s_%s_%s", keyStr.substring(0,len-midStr.length()-hash.length()-3),midStr, key.hashCode());
		}
		return keyStr;
	}

}
