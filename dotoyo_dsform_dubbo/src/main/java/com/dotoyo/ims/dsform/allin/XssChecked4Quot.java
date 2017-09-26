package com.dotoyo.ims.dsform.allin;

/**
 * &quot;	"
 * @author wangl
 *
 */
public class XssChecked4Quot implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("&quot;".equals(key)){
			return key;
		}
		return "";
	}
}
