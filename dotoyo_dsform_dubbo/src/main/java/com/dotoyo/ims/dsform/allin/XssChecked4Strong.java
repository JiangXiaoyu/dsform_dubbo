package com.dotoyo.ims.dsform.allin;

/**
 * <strong>黑体效果</strong>
 * @author wangl
 *
 */
public class XssChecked4Strong implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		if("<strong>".equals(key) || "</strong>".equals(key)){
			return key;
		}
		return "";
	}
}
