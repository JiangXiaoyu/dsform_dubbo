package com.dotoyo.ims.dsform.allin;

/**
 * &lsquo;    ‘
 * @author wangl
 *
 */
public class XssChecked4Lsquo implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&lsquo;".equals(key)){
			return key;
		}
		return "";
	}
}
