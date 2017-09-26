package com.dotoyo.ims.dsform.allin;

/**
 * <u>下划线效果</u>
 * @author wangl
 *
 */
public class XssChecked4U implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<u>".equals(key) || "</u>".equals(key)){
			return key;
		}
		return "";
	}
}
