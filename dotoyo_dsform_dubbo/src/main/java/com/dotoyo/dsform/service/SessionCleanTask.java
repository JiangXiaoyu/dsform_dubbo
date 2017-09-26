package com.dotoyo.dsform.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractQuartzJob;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.FrameSessionListener;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameSessionDataSave;
import com.dotoyo.ims.dsform.allin.IFrameStaticData;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.SessionData;
import com.dotoyo.ims.dsform.allin.SessionUtil;

public class SessionCleanTask extends AbstractQuartzJob {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SessionCleanTask.class));
	private final static long SIGN_TIME_OUT = 120 * 1000;// 签到失效时间

	public void doTask(Map<String, String> param) {
		Map<String, HttpSession> sessionMap = FrameSessionListener.getSession();
		log.debug(sessionMap);
		Iterator<Entry<String, HttpSession>> iterator = sessionMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, HttpSession> entry = iterator.next();
			String key = entry.getKey();
			HttpSession s = entry.getValue();
			doTask4Item(key, s, param);
		}
	}

	protected void doTask4Item(String key, HttpSession s,
			Map<String, String> param) {

		try {
			//
			IFrameSessionDataSave save = SessionUtil.getFrameSessionDataSave();
			Map<String, String> p = new HashMap<String, String>();
			p.put("sessionId", key);
			SessionData sessionData = save.getLoginUser(p);
			if (sessionData == null) {
				return;
			}
			//
			long sessionTimeOut = s.getMaxInactiveInterval() * 1000;// 毫秒
			long now = System.currentTimeMillis();
			long time = now - sessionData.getLastAccessTime();
			//
			if (time > sessionTimeOut) {

				s.invalidate();
				return;

			}
			// 心跳会话关闭
			if (isCheckHeartbeat()) {
				time = now - sessionData.getLastSignTime();
				if (time > SIGN_TIME_OUT) {
					s.invalidate();
					return;

				}
			}

		} catch (IllegalStateException e) {
			log.error("", e);
		} catch (Throwable e) {
			log.error("", e);
		}

	}

	private boolean isCheckHeartbeat() {
		try {
			IFrameStaticData data = FrameFactory.getStaticDataFactory(null);
			String d = data.getStaticData("heartbeat", "checkHeartbeat");
			boolean ret = StringsUtils.getBoolean(d);
			return ret;
		} catch (Throwable e) {
			log.error("", e);
			return true;
		}

	}
}
