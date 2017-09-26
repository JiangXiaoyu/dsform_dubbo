package com.dotoyo.ims.dsform.allin;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 定时任务抽象类:用于系统内部定时任务
 * 
 * @author xieshh
 * 
 */
abstract public class AbstractQuartzJob extends QuartzJobBean implements ITask {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(AbstractQuartzJob.class));

	protected void executeInternal(JobExecutionContext ctx) {

		executeInternal4Task(ctx);

	}

	protected void executeInternal4Task(JobExecutionContext ctx) {

		if (isRuning) {
			return;
		} else {

			if (setRuning()) {
				try {
					Map<String, String> param = new Hashtable<String, String>();
					JobDataMap map = ctx.getMergedJobDataMap();

					for (Iterator<?> it = map.keySet().iterator(); it.hasNext();) {
						Object key = it.next();
						if (key == null) {
							continue;
						}
						if (key instanceof String) {
							String temp = (String) key;
							param.put(temp, map.getString(temp));
						}

					}
					try {
						param.put("clusterId", FrameConfig.getInstance()
								.getConfig("clusterId"));
					} catch (Throwable e) {
						log.error("", e);
					}
					doTask(param);
				} finally {
					isRuning = false;
				}
			}

		}
	}

	private static boolean isRuning = false;

	synchronized static private boolean setRuning() {

		if (isRuning) {
			return false;
		}
		isRuning = true;
		return true;

	}

	abstract public void doTask(Map<String, String> param);

}
