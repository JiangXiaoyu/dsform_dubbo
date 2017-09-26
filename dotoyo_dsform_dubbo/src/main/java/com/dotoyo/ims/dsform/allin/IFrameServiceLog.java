package com.dotoyo.ims.dsform.allin;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * 业务日志
 * 
 * @author xieshh
 * 
 */
public interface IFrameServiceLog {
	/**
	 * 操作类型定义
	 */
	public String codes[] = { "insert", "update", "delete", "query","error" };

	public static final String INSERT = codes[0];
	public static final String UPDATE = codes[1];
	public static final String DELETE = codes[2];
	public static final String QUERY = codes[3];
	public static final String ERROR = codes[4];

	/**
	 * 记录了什么人，什么时候，在什么机器，对什么业务[action与method决定一个业务]，做了什么操作[type],操作明细[detail]
	 * 
	 * @param type
	 * @param action
	 * @param method
	 * @param msg
	 */
	public void addServiceLog(String type, Class<?> action, String method,
			String detail);

	/**
	 * 获得日记队列
	 */
	public BlockingQueue<Map<String, Object>> getBlockingQueue();
}
