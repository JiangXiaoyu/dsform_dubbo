package com.dotoyo.ims.dsform.allin;

/**
 * <s> 删除线效果</s>
 * @author wangl
 *
 */
public class XssChecked4S implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<s>".equals(key) || "</s>".equals(key)){
			return key;
		}

		return "";
	}
}
