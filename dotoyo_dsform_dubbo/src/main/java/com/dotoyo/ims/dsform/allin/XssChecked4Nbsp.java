package com.dotoyo.ims.dsform.allin;

public class XssChecked4Nbsp implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&nbsp;".equals(key)){
			return key;
		}
		return "";
	}
}
