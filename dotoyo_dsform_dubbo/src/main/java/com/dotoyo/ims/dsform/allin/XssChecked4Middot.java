package com.dotoyo.ims.dsform.allin;

public class XssChecked4Middot implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&middot;".equals(key)){
			return key;
		}
		return "";
	}
}
