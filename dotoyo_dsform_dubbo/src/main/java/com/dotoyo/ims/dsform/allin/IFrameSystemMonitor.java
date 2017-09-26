package com.dotoyo.ims.dsform.allin;

/**
 * 系统监控模块
 * 
 * @author wangl
 * 
 */
public interface IFrameSystemMonitor {
	public void addMonitor(IMonitorTask task) throws Exception;
	public void remoteMonitor(IMonitorTask task) throws Exception;
}
