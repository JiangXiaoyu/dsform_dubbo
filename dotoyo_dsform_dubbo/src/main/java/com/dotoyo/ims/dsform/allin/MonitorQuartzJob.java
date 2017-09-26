package com.dotoyo.ims.dsform.allin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

final public class MonitorQuartzJob extends AbstractQuartzJob {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MonitorQuartzJob.class));

	private static Map<IMonitorTask, String> taskMap = new ConcurrentHashMap<IMonitorTask, String>();

	public static void addTask(IMonitorTask task) {
		taskMap.put(task, "");
	}

	public static void removeTask(IMonitorTask task) {
		taskMap.remove(task);
	}

	public void doTask(Map<String, String> param) {
		try {
			for (IMonitorTask task : taskMap.keySet()) {
				try {
					task.doMonitor();
				} catch (Throwable e) {
				}
			}

		} catch (Throwable e) {
			log.error("", e);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
