package com.dotoyo.ims.dsform.allin;

/**
 * &rdquo; ”
 * @author wangl
 *
 */
public class XssChecked4Rdquo implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&rdquo;".equals(key)){
			return key;
		}
		return "";
	}
}
