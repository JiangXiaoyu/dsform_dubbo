package com.dotoyo.dsform.service;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSessionRequest;
import com.dotoyo.dsform.log.LogProxy;

abstract public class AbstractSessionRequest implements IFrameSessionRequest {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractSessionRequest.class));

}
