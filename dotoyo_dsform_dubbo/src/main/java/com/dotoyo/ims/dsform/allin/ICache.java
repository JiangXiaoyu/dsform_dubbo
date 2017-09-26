package com.dotoyo.ims.dsform.allin;

import org.apache.ibatis.cache.Cache;

public interface ICache extends Cache {
	public void clear4NoCycle();
	public boolean isUserCache();
}
