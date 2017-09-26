package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 * 随机数控件
 */
public class RndCalWidge implements IWidgeType{

	public static final String WIDGE_NAME = "rndcal".toLowerCase();

	private IWidgeType widge = null;
	
	public RndCalWidge(IWidgeType widge) {
		this.widge = widge;
	}
	
	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb,
			float width, float height, String tagId, String textAlign,
			String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			
			String value = attrs.get("value")!=null ? attrs.get("value") : "";
			String min = attrs.get("min")!=null ?  attrs.get("min").toLowerCase() : "";
			String max = attrs.get("max")!=null ? attrs.get("max").toLowerCase() : "";
			String src = attrs.get("src")!=null ? attrs.get("src").toLowerCase() : "";//来源
			//String text = attrs.get("text")!=null ? attrs.get("text") : "";//显示
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
					"<input id='%s' name='%s' type='text' widgeType='%s' min='%s' max='%s' src='%s' cal='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;background-color:%s;%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s'></input>",
					tagId, tagId, WIDGE_NAME, min, max, src, value, width, textAlign, PreformConstant.BACKGROUND_COLOR_3, fontStyle, length));
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
	public String append2ViewHtml(String widgeType, StringBuilder sb,
			float width, float height, String tagId, String textAlign,
			String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			
			String value = attrs.get("value")!=null ? attrs.get("value") : "";
			String min = attrs.get("min")!=null ?  attrs.get("min").toLowerCase() : "";
			String max = attrs.get("max")!=null ? attrs.get("max").toLowerCase() : "";
			String src = attrs.get("src")!=null ? attrs.get("src").toLowerCase() : "";//来源
			//String text = attrs.get("text")!=null ? attrs.get("text") : "";//显示
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' min='%s' max='%s' src='%s' cal='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s' size='%s'>%s</span>",
					tagId, tagId, WIDGE_NAME, min, max, src, value, width, textAlign, fontStyle, length, value));
			return WIDGE_NAME;
		}else{
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
						textAlign, fontStyle);
		}
	}

	@Override
	public boolean validate(String widgeType) {
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType) {
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
