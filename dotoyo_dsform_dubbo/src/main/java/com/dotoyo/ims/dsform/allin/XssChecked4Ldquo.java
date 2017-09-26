package com.dotoyo.ims.dsform.allin;

/**
 * &hellip;    …  
 * @author wangl
 *
 */
public class XssChecked4Ldquo implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&ldquo;".equals(key)){
			return key;
		}
		return "";
	}
}
