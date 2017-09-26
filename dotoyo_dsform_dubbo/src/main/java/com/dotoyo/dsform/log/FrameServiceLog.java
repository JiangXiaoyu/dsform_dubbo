package com.dotoyo.dsform.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.ims.dsform.allin.AbstractFrame;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameAuthority;
import com.dotoyo.ims.dsform.allin.IFrameKeyvalue;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSequence;
import com.dotoyo.ims.dsform.allin.IFrameServiceLog;

public class FrameServiceLog extends AbstractFrame implements IFrameServiceLog {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameServiceLog.class));
	protected static FrameServiceLog instance = null;

	protected FrameServiceLog() {

	}

	private BlockingQueue<Map<String, Object>> blockingQueue = new ArrayBlockingQueue<Map<String, Object>>(
			10000);

	public static FrameServiceLog getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return (FrameServiceLog) instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameServiceLog();
		}
	}

	public void startup() {

	}

	/**
	 * 记录了什么人，什么时候，在什么机器，对什么业务[action与method决定一个业务]， 做了什么操作[type],操作明细[detail]
	 */
	public void addServiceLog(String type, Class<?> action, String method,
			String msg) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		Map<String, Object> map = null;
		try {
			String userName = getUserName();
			String result = "用户:" + userName + ",在" + dateStr + ",对"
					+ action.getName() + "的" + method + "方法,执行了" + type
					+ "操作,明细:" + msg + ".";
			IFrameSequence sF = FrameFactory.getSequenceFactory(null);
			String id = sF.getSequence();
			map = new HashMap<String, Object>();
			map.put("userId", getUserId());
			map.put("userCode", getUserCode());
			map.put("actionName", action.getName());
			map.put("methodName", method);
			map.put("createTime", date);
			map.put("detail", result);
			map.put("id", id);
			map.put("thisType", type);
			map.put("ip", getUserIp());
			map.put("module", getModule(action.getName()));
			
			//只考虑错误的日志
			if(IFrameServiceLog.ERROR.equals(type)){
				blockingQueue.offer(map);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/*
	 * 获取当前所属模块（未实现）
	 */
	private String getModule(String actionName) throws Exception {
		IFrameKeyvalue factory = FrameFactory.getKeyValueFactory(null);
		String module = "1";
		String code = "module";
		List<Map<String, String>> result = factory
				.getKeyValueList(module, code);
		String value = "";
		for (Map<String, String> map : result) {
			String v = map.get("VALUE");
			if (actionName.equals(v)) {
				value = map.get("TEXT");
			}
		}
		return value;
	}

	/*
	 * 获取用户ip
	 */
	private String getUserIp() throws Exception {
		IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
		return sf.getLoginUserIp(null);
	}

	/*
	 * 获取用户id
	 */
	private String getUserId() throws Exception {
		IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
		return sf.getLoginUserId(null);
	}

	/*
	 * 获取用户编码
	 */
	private String getUserCode() throws Exception {
		IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
		return sf.getLoginUserCode(null);
	}

	/*
	 * 获取用户name
	 */
	private String getUserName() throws Exception {
		IFrameAuthority sf = FrameFactory.getAuthorityFactory(null);
		return sf.getLoginUserName(null);
	}

	public static void main(String[] args) throws Exception {
		IFrameServiceLog serviceLog = null;
	
			serviceLog = FrameFactory.getServiceLogFactory(null);
		

		serviceLog.addServiceLog("新增", FrameServiceLog.class, "main",
				"新增了一个设诉[58976]的定义");
	}

	public void shutdown() {
		instance = null;
	}

	public BlockingQueue<Map<String, Object>> getBlockingQueue() {
		return blockingQueue;
	}
}
