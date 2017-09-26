package com.dotoyo.ims.dsform.allin;

/**
 * <li> </li> 
 * @author wangl
 *
 */
public class XssChecked4Ul implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<ul>".equals(key) || "</ul>".equals(key)){
			return key;
		}
		return "";
	}
}
