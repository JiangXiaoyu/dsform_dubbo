package com.dotoyo.ims.dsform.allin;

public class XssChecked4P implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<p>".equals(key) || "</p>".equals(key)){
			return key;
		}
		
		if(key.startsWith("<p style=\"text-align:")){
			return key;
		}
		return "";
	}
}
