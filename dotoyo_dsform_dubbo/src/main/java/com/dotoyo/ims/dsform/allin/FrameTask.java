package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class FrameTask extends AbstractFrame implements IFrameTask {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameTask.class));

	protected static FrameTask instance = null;

	protected FrameTask() {

	}

	public static FrameTask getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new FrameTask();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public void startup() {
		// 判断当前是否是主机，如果是打开定时任务
		String taskWorking = FrameFactory.getTaskWorking();

		if (StringsUtils.isEmpty(taskWorking)) {
			working = true;
		} else {
			working = StringsUtils.getBoolean(taskWorking);
		}

	}

	public void shutdown() {

	}

	public boolean isStartup() {

		return false;
	}

	private boolean working = false;

	public boolean isWorking() throws Exception {
		return working;
	}

	public void setWorking(boolean flag) throws Exception {
		working = flag;
	}
}
