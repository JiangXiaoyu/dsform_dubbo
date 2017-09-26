
package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class FrameMail extends AbstractFrame implements IFrameMail {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameMail.class));

	protected static FrameMail instance = null;

	protected FrameMail() {

	}

	public static FrameMail getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameMail();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	public void sendMain(Map<String, String> param) throws Exception {
		//发邮件代码
		
	}

}
