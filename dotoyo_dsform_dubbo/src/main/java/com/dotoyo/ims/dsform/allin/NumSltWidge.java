package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 计算数据来源
 */
public class NumSltWidge implements IWidgeType {

	public static final String WIDGE_NAME = "numslt".toLowerCase();

	private IWidgeType widge = null;

	public NumSltWidge(IWidgeType widge) {
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
			
			String[] array = val.split(",");
			sb.append(String.format(
						"<select id='%s' name='%s' widgeType='%s' size='%s' oninput='calc(this);' onpropertychange='calc(this);' style='padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;%s'>",
						tagId, tagId, WIDGE_NAME, length, width, textAlign, fontStyle));
			for(String str : array){
				Pattern pattern = Pattern.compile("\\d+\\.?\\d+");
			    Matcher matcher = pattern.matcher(str);  
			    if (matcher.find()){ 
			       String value = matcher.group(0);
			       sb.append(String.format("<option value='%s'>%s</option>", value, str));
			    }
			}
			sb.append("</select>");
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
			width = width-2-2;//表格长度-padding-边框
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
						"<span id='%s' name='%s' type='text' widgeType='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'></span>",
						tagId, tagId, WIDGE_NAME, length, width, textAlign, fontStyle));
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
