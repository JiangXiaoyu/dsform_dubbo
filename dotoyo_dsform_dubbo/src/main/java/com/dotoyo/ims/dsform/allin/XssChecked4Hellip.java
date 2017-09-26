package com.dotoyo.ims.dsform.allin;

/**
 * &hellip;		
 * @author wangl
 *
 */
public class XssChecked4Hellip implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&hellip;".equals(key)){
			return key;
		}
		return "";
	}
}
