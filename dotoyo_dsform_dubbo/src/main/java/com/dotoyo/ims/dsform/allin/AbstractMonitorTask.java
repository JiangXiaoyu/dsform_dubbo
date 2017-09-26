package com.dotoyo.ims.dsform.allin;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public abstract class AbstractMonitorTask implements IMonitorTask {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractMonitorTask.class));
}
