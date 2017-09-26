package com.dotoyo.ims.dsform.allin;

/**
 * &mdash;	 — 
 * @author wangl
 *
 */
public class XssChecked4Mdash implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&mdash;".equals(key)){
			return key;
		}
		return "";
	}
}
