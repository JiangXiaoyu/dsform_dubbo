package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/**
 * 隐藏控件,无实际意义
 */
public class HiddenWidge implements IWidgeType {

	public static final String WIDGE_NAME = "hidden".toLowerCase();

	private IWidgeType widge = null;

	public HiddenWidge(IWidgeType widge) {
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			String val = attrs.get("val");//显示值
			sb.append(String.format(
					"<input id='%s' name='%s' type='hidden' widgeType='%s' style='padding:1px;width:%spx;text-align:%s;%s' size='%s' value='%s'/>",
					tagId, tagId, WIDGE_NAME, width, textAlign, fontStyle, length/2, val));
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			String val = attrs.get("val");//显示值
			sb.append(String.format(
					"<input id='%s' name='%s' type='hidden' widgeType='%s' style='padding:1px;width:%spx;text-align:%s;%s' size='%s' value='%s'/>",
					tagId, tagId, WIDGE_NAME, width, textAlign, fontStyle, length/2, val));
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
