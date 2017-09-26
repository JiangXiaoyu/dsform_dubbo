package com.dotoyo.ims.dsform.allin;

public interface IFrameHeartbeat {
	/**
	 * 向目标主机发送HTTP心跳请求
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void sendHeartbeat(HeartbeatBo bo) throws Exception;
}
