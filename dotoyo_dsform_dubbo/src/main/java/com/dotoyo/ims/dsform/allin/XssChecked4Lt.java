package com.dotoyo.ims.dsform.allin;

public class XssChecked4Lt implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&lt;".equals(key)){
			return key;
		}
		return "";
	}
}
