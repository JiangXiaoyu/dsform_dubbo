package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import com.dotoyo.dsform.util.StringsUtils;

/**
 * 计算数据来源
 */
public class NumTextWidge implements IWidgeType {

	public static final String WIDGE_NAME = "numtext".toLowerCase();

	private IWidgeType widge = null;

	public NumTextWidge(IWidgeType widge) {
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
			String val = attrs.get("val")!=null ? attrs.get("val") : "";//计算值
			String text = attrs.get("text")!=null ?  attrs.get("text") : "";//显示值
			
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			if(StringsUtils.isEmpty(val) && StringsUtils.isEmpty(text)){
				sb.append(String.format(
						"<input id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' oninput='calc(this);' onpropertychange='calc(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;background-color:%s;%s'/>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, PreformConstant.BACKGROUND_COLOR_3, fontStyle));
			}else{
				sb.append(String.format(
						"<span id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'>%s</span>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, fontStyle, text));
			}
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
			String val = attrs.get("val")!=null ? attrs.get("val") : "" ;//计算值
			String text = attrs.get("text")!=null ? attrs.get("text") : "";//显示值
			
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
						"<span id='%s' name='%s' type='text' widgeType='%s' val='%s' text='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'>%s</span>",
						tagId, tagId, WIDGE_NAME, val, text, length, width, textAlign, fontStyle, text));
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
