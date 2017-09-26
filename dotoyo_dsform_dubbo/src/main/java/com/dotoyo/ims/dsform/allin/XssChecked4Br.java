package com.dotoyo.ims.dsform.allin;

public class XssChecked4Br implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<br>".equals(key)){
			return key;
		}
		
		if("</br>".equals(key)){
			return key;
		}
		
		if("<br/>".equals(key)){
			return key;
		}
		if("<br />".equals(key)){
			return key;
		}
		
		return "";
	}
}
