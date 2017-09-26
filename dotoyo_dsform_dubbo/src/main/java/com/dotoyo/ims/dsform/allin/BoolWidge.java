package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;


/**
 * 最终计算结果显示(也可以当做计算来源)
 */
public class BoolWidge implements IWidgeType {

	public static final String WIDGE_NAME = "bool".toLowerCase();

	private IWidgeType widge = null;

	public BoolWidge(IWidgeType widge) {
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
			
			String exps = attrs.get("exps").toLowerCase();//公式
			//overflow:hidden;display:inline-block;white-space:nowrap;
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' exps='%s' style='overflow:hidden;display:inline-block;white-space:nowrap; padding:1px; width:%spx; text-align:%s; background-color:%s; %s' size='%s'></span>",
					tagId, tagId, WIDGE_NAME, exps, width, textAlign, PreformConstant.BACKGROUND_COLOR_4, fontStyle, length/2));
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
			
			String exps = attrs.get("exps").toLowerCase();;//公式
			//overflow:hidden;display:inline-block;white-space:nowrap;
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' exps='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s' size='%s'></span>",
					tagId, tagId, WIDGE_NAME, exps,  width, textAlign, fontStyle, length/2));
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
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME+"[")){
			return true;
		}
		return false;
	}
}
