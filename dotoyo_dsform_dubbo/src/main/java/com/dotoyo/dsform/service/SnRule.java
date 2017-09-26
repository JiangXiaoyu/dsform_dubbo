package com.dotoyo.dsform.service;

import com.dotoyo.dsform.model.SerialNum;

/*
 * sn规则(默认值,补位,循环)
 * 
 * //默认001,位数为三位.
 */
public class SnRule implements ISnRule{
	
	public SerialNum sn;
	
	public SnRule(SerialNum sn){
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
