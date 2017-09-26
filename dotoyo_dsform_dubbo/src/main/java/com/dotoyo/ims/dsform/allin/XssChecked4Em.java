package com.dotoyo.ims.dsform.allin;

/**
 * <em>斜体效果</em>
 * @author wangl
 *
 */
public class XssChecked4Em implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<em>".equals(key) || "</em>".equals(key)){
			return key;
		}
		return "";
	}
}
