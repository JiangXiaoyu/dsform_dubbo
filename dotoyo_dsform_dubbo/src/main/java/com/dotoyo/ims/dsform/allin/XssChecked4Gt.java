package com.dotoyo.ims.dsform.allin;

public class XssChecked4Gt implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&gt;".equals(key)){
			return key;
		}
		return "";
	}
}
