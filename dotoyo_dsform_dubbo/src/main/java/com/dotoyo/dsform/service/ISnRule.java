package com.dotoyo.dsform.service;


/*
 * sn规则
 */
public interface ISnRule {
	/*
	 * 判断是否需要循环
	 */
	public boolean isCycle();
	
	/*
	 * 设置起始默认值
	 */
	public void setDefaultValue();
	
	/*
	 * 设置下一个值
	 */
	public void setNextValue();
}
