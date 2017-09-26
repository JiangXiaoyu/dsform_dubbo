package com.dotoyo.ims.dsform.allin;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 用户许可存在缓存中
 * 
 * @author xieshh
 * 
 */
abstract public class AbstractSessionDataSaveBean implements
		IFrameSessionDataSave {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractSessionDataSaveBean.class));

}
