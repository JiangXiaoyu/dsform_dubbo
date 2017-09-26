package com.dotoyo.ims.dsform.allin;

/**
 * <li> </li> 
 * @author wangl
 *
 */
public class XssChecked4Ol implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<ol>".equals(key) || "</ol>".equals(key)){
			return key;
		}
		return "";
	}
}
