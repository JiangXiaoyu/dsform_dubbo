package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * 用于测试框架的心跳实现类，也可当作自定义心跳实现类的模板
 * 
 * @author xieshh
 * 
 */
public class FrameHeartbeat extends AbstractFrame implements IFrameHeartbeat {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameHeartbeat.class));

	protected static FrameHeartbeat instance = null;

	protected FrameHeartbeat() {

	}

	public static FrameHeartbeat getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameHeartbeat();
		}
	}

	public void sendHeartbeat(HeartbeatBo bo) throws Exception {
		throw new FrameException("3000000000000006", null);
	}

	public void startup() {

	}

	public void shutdown() {

	}
}
