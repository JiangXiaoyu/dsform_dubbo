package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/**
 * 强度等级(strength grade)
 * ${strgrd[val=m]} --- m表示砂浆强度
 * ${strgrd[val=c]} --- c表示钢筋混凝土强度
 * ${strgrd[val=b]} --- b表示混凝土抗折(公路)
 * @author wangl
 */
public class StrGrdWidge implements IWidgeType {
	private static final String WIDGE_NAME = "strgrd".toLowerCase();
	private IWidgeType nextWidge = null;

	public StrGrdWidge(IWidgeType widge) {
		this.nextWidge = widge;
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
			width = width-2-2;//表格长度-padding-边框
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String val = attrs.get("val").toLowerCase();
			sb.append(String.format(
					"<select id='%s' name='%s' type='select' widgeType='%s' val='%s' onchange='CChangeEvent(this);' style='padding:2px;width:%spx;' size='%s'>",
					tagId, tagId, WIDGE_NAME, val, width, length));
			sb.append("</select>");
			return WIDGE_NAME;
		} else {
			if(nextWidge == null){
				return "";
			}
			return nextWidge.append2EditHtml(widgeType, sb, width, height, tagId,
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String val = attrs.get("val").toLowerCase();
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' val='%s' style='padding:1px;width:%spx;text-align:%s;' size='%s'></span>",
					tagId, tagId, WIDGE_NAME, val ,width, textAlign, length));
			return WIDGE_NAME;
		} else {
			if(nextWidge == null){
				return "";
			}
			return nextWidge.append2ViewHtml(widgeType, sb, width, height, tagId,
					textAlign, fontStyle);
		}
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME+'[')){
			return true;
		}
		return false;
	}
}

