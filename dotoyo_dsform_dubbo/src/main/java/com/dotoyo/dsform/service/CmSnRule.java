package com.dotoyo.dsform.service;

import com.dotoyo.dsform.model.SerialNum;

/*
 * cmsn规则
 * //默认001
 * //两个格子时,出现三位数情况时,第一个格子显示两位,第二个格子显示剩余的一位.
 */
public class CmSnRule implements ISnRule{
	
	public SerialNum sn;
	
	public CmSnRule(SerialNum sn){
		this.sn = sn;
	}
	
	//默认的保存值
	private int DEFAULT_SAVE_VALUE = 1;

	@Override
	public void setNextValue() {
		if(isCycle()){
			setDefaultValue();
		}else{
			sn.setCurValue(sn.getCurValue() + 1);
		}
	}

	//如果超过999,则循环
	@Override
	public boolean isCycle() {
		if(sn.getCurValue() == 999){
			return true;
		}
		return false;
	}

	@Override
	public void setDefaultValue() {
		sn.setCurValue(DEFAULT_SAVE_VALUE);
	}
}
