package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * WEB请求监控实现类
 * 
 * @author xieshh
 * 
 */
public class FrameRequestMonitor extends AbstractMonitorTask {
	private HttpServletRequest request = null;
	private Throwable t = new Throwable();
	private long start = System.currentTimeMillis();

	public FrameRequestMonitor(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 
	 */
	public void doMonitor() {
		long now = System.currentTimeMillis();
		long time = (now - start) / 1000;
		if (time > 120) {

			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("request", request.toString());
				map.put("time", time + "");
				;
				log.warn(FrameUtils.getWords("1000000000000006", "", map), t);
			} catch (Throwable e) {

			}

		}
	}
}
