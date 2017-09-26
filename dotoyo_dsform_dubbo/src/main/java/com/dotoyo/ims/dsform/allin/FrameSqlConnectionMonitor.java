package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

public class FrameSqlConnectionMonitor extends AbstractMonitorTask {
	private FrameSqlConnection conn = null;
	private Throwable t = new Throwable();
	private long start = System.currentTimeMillis();

	public FrameSqlConnectionMonitor(FrameSqlConnection conn) {
		this.conn = conn;
	}

	/**
	 * 
	 */
	public void doMonitor() {
		long now = System.currentTimeMillis();
		long time=(now-start)/1000;
		if (time > 60) {
			
			try {
				Map<String, String> map=new HashMap<String, String>();
				map.put("conn", conn.toString());
				map.put("time", time+"");
				;
//				log.warn(FrameUtils.getWords("1000000000000005", "", map), t);
			} catch (Throwable e) {
				
			}
			
		}
	}
}
