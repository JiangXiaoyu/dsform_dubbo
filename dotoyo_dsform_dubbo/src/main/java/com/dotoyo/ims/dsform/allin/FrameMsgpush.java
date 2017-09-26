package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

import net.sf.json.JSONArray;

/**
 * 单节点消息推送模块实现:后续改造为使用消息队列，使支持多单点
 * 
 * @author xieshh
 * 
 */
public class FrameMsgpush extends AbstractFrame implements IFrameMsgpush {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameMsgpush.class));

	protected static FrameMsgpush instance = null;

	protected FrameMsgpush() {

	}

	public static FrameMsgpush getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameMsgpush();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	protected static Map<String, BlockingQueue<String>> userMsgs = new ConcurrentHashMap<String, BlockingQueue<String>>();

	/**
	 * 取得当前用户最多10条的可用的消息
	 * 
	 * @param user
	 * @return
	 * @throws InterruptedException
	 */
	public JSONArray getMsg4Wait(UserLoginBo user) throws Exception {
		if(user==null){
			return null;
		}
		JSONArray ret = new JSONArray();
		String userCode = user.getUserCode();
		BlockingQueue<String> msgs = userMsgs.get(userCode);
		if (msgs == null) {
			return null;
		}
		String msg = msgs.poll(5, TimeUnit.SECONDS);
		if (msg != null) {
			ret.add(msg);
			for (int i = 0; i < 9; i++) {
				msg = msgs.poll(0, TimeUnit.SECONDS);
				if (msg != null) {
					ret.add(msg);
				} else {
					return ret;
				}
			}
			return ret;
		} else {
			return null;
		}

	}

	public void messagePush(String userCode, String msg) throws Exception {
		BlockingQueue<String> msgs = userMsgs.get(userCode);
		if (msgs == null) {
			msgs = login(userCode);
		}
		if (!msgs.offer(msg, 5000, TimeUnit.SECONDS)) {
			// 推送缓冲区已满
			FrameException e = new FrameException("3000000000000016",
					new HashMap<String, String>());
			throw e;
		}

	}

	synchronized public BlockingQueue<String> login(String userCode)
			throws Exception {
		BlockingQueue<String> msgs = userMsgs.get(userCode);
		if (msgs == null) {
			msgs = new ArrayBlockingQueue<String>(100);
			userMsgs.put(userCode, msgs);
		}
		return msgs;
	}

	synchronized public void logout(String userCode) throws Exception {
		BlockingQueue<String> msgs = userMsgs.get(userCode);
		if (msgs != null) {
			userMsgs.remove(userCode);
		}

	}
}
