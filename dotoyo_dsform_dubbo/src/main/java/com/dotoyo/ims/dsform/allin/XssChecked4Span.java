package com.dotoyo.ims.dsform.allin;

import java.util.regex.Pattern;

/**
 * <span style="font-size:22px"><span style="font-family:宋体">一一</span></span>
 * @author wangl
 *
 */
public class XssChecked4Span implements IXssChecked{

	@Override
	public String check(String key) throws Exception {
		key = key.toLowerCase();
		if("</span>".equals(key)){
			return key;
		}
		if(key.startsWith("<span style=\"font-size:")){
			Pattern pattern1 = Pattern.compile("<span style=\"font-size:\\d*px\">");
			if(pattern1.matcher(key).matches()){
				return key;
			}
		}else if(key.startsWith("<span style=\"font-family:") || key.startsWith("<span style=\"color:")){
			return key;
		}
		return "";
	}
}
