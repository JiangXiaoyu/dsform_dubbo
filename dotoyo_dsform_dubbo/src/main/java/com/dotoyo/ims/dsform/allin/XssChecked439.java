package com.dotoyo.ims.dsform.allin;

/**
 * &#39;   
 * @author wangl
 *
 */
public class XssChecked439 implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&#39;".equals(key)){
			return key;
		}
		return "";
	}
}
