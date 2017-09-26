package com.dotoyo.ims.dsform.allin;

/**
 * <h1> <h2> <h3> <h4>
 * @author wangl
 *
 */
public class XssChecked4H implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<h1>".equals(key) || "</h1>".equals(key)){
			return key;
		}
		if("<h2>".equals(key) || "</h2>".equals(key)){
			return key;
		}
		if("<h3>".equals(key) || "</h3>".equals(key)){
			return key;
		}
		if("<h4>".equals(key) || "</h4>".equals(key)){
			return key;
		}
		return "";
	}
}
