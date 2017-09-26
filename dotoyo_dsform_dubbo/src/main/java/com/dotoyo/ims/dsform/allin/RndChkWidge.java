package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 * 随机数单选控件
 */
public class RndChkWidge implements IWidgeType{

	public static final String WIDGE_NAME = "rndchk".toLowerCase();

	private IWidgeType widge = null;
	
	public RndChkWidge(IWidgeType widge) {
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
			width = width-2-2;//表格长度-padding-边框
			String min = attrs.get("min")!= null?attrs.get("min").toLowerCase():"";
			String max = attrs.get("max")!= null?attrs.get("max").toLowerCase():"";
			String text = attrs.get("text")!= null?attrs.get("text"):"";//显示值
			String rng = attrs.get("rng")!= null?attrs.get("rng").toLowerCase():"";//关联
			String alias = attrs.get("alias")!= null?attrs.get("alias"):tagId;//alias
			sb.append(String.format(
					"<input id='%s' name='%s' type='checkbox' alias='%s' enabled='enabled' widgeType='%s' onclick='radioOnlyEvent(this);' style='padding:1px;%s' min='%s' max='%s' rng='%s'/>%s",
					tagId, tagId, alias, WIDGE_NAME, fontStyle, min, max, rng, text));
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
			width = width-2-2;//表格长度-padding-边框
			String min = attrs.get("min")!= null?attrs.get("min"):"";
			String max = attrs.get("max")!= null?attrs.get("max"):"";
			String text = attrs.get("text")!= null?attrs.get("text"):"";//显示值
			String rng = attrs.get("rng")!= null?attrs.get("rng"):"";//显示值
			String alias = attrs.get("alias")!= null?attrs.get("alias"):tagId;//alias
			sb.append(String.format(
					"<input id='%s' name='%s' type='checkbox' alias='%s' disabled='disabled' widgeType='%s' onclick='rndChkEvent(this);' style='padding:1px;%s' min='%s' max='%s' rng='%s'/>%s",
					tagId, tagId, alias, WIDGE_NAME, fontStyle, min, max, rng, text));
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
