package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

public class OrgWidge implements IWidgeType {

	public static final String WIDGE_NAME = "org".toLowerCase();

	private IWidgeType widge = null;

	public OrgWidge(IWidgeType widge) {
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
			int length = 0;
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				String lenStr = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}
			//valign 默认 bottom
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;'>"+
							"<div id='%s' name='%s' widgeType='%s' class='%s' isTextarea='true' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							" text-align:%s; vertical-align: bottom; word-wrap: break-word; word-break: break-all;background-color:%s;%s' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' title='自动获取机构名'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, "orgPro", width, height, textAlign, PreformConstant.BACKGROUND_COLOR_1, fontStyle, length/2));
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
			int length = 0;
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				String lenStr = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;'>"+
							"<div id='%s' name='%s' widgeType='%s' class='%s' isTextarea='false' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							" text-align:%s; vertical-align: bottom; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width,height,tagId,tagId,WIDGE_NAME,WIDGE_NAME,width, height,textAlign,fontStyle,length/2));
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
		if (widgeType.toLowerCase().indexOf(WIDGE_NAME) != -1){
			return true;
		}
		return false;
	}
}
