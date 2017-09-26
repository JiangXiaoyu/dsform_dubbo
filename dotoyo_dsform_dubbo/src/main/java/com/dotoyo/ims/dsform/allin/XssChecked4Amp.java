package com.dotoyo.ims.dsform.allin;

/**
 * &amp;     & 
 * @author wangl
 *
 */
public class XssChecked4Amp implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&amp;".equals(key)){
			return key;
		}
		return "";
	}
}
