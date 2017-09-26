package com.dotoyo.ims.dsform.allin;
/**
 * 
 * <div style="text-align: center;">asdasdf</div>
 */


public class XssChecked4Div implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("<div>".equals(key) || "</div>".equals(key)){
			return key;
		}
		
		if(key.startsWith("<div style=\"text-align:")){
			return key;
		}
		return "";
	}
}
