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
public class AbstractFrameSystemMonitor extends AbstractFrame implements
		IFrameSystemMonitor {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractFrameSystemMonitor.class));

	protected static AbstractFrameSystemMonitor instance = null;

	protected AbstractFrameSystemMonitor() {

	}

	public static AbstractFrameSystemMonitor getInstance(
			Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new AbstractFrameSystemMonitor();
		}
	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void addMonitor(IMonitorTask task) throws Exception {
		if(task!=null){
			MonitorQuartzJob.addTask(task);
		}
		
	}

	public void remoteMonitor(IMonitorTask task) throws Exception {
		if(task!=null){
			MonitorQuartzJob.removeTask(task);
		}
		
	}

}
