package com.dotoyo.ims.dsform.allin;

/**
 * <ul> </ul> 
 * @author wangl
 *
 */
public class XssChecked4Li implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<li>".equals(key) || "</li>".equals(key)){
			return key;
		}
		return "";
	}
}
