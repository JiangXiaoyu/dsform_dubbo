package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;


public class WeatherWidge implements IWidgeType {

	public static final String WIDGE_NAME = "weather".toLowerCase();

	private IWidgeType widge = null;

	public WeatherWidge(IWidgeType widge) {
		this.widge = widge;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			int length = 8;
			width = width-2-2;//表格长度-padding-边框
			sb.append(String.format(
					"<input id='%s' name='%s' type='text' widgeType='%s' size='%s' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' style='padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;%s' title='自动获取天气'/>",
					tagId, tagId, WIDGE_NAME, length, width,textAlign,fontStyle));
			return WIDGE_NAME;
		} else {
			if(widge == null){
				return "";
			}
			return widge.append2EditHtml(widgeType, sb, width, height, tagId,
					textAlign, fontStyle);
		}
	}

	@Override
	public String append2ViewHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			int length = 8;
			width = width-2-2;//表格长度-padding-边框
			sb.append(String.format(
							"<span id='%s' name='%s' type='text' widgeType='%s' style='padding:1px;width:%spx;text-align:%s;%s' size='%s'></span>",
							tagId, tagId, WIDGE_NAME, width,textAlign, fontStyle, length));
			return WIDGE_NAME;
		} else {
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
					textAlign, fontStyle);
		}
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
