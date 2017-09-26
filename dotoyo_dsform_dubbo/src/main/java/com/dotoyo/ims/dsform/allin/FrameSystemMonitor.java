package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;


/**
 * 系统监控模块
 * 
 * @author wangl
 * 
 */
public class FrameSystemMonitor extends AbstractFrameSystemMonitor {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameSystemMonitor.class));

	protected static FrameSystemMonitor instance = null;

	protected FrameSystemMonitor() {

	}

	public static FrameSystemMonitor getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new FrameSystemMonitor();
		}
	}

}
