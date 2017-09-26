
package com.dotoyo.ims.dsform.allin;

/**
 * 队列满时当前线程直接运行任务
 * 
 * @author xieshh
 * 
 */
public interface IFrameThread extends IFrame{

	/**
	 * 在指定的线程池运行指定的任务
	 * 
	 * @param task
	 */
	public void execute(String code, Runnable task);


}
