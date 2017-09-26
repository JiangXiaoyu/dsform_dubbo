package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 * 随机数控件
 */
public class RndValWidge implements IWidgeType{

	public static final String WIDGE_NAME = "rndval".toLowerCase();

	private IWidgeType widge = null;
	
	public RndValWidge(IWidgeType widge) {
		this.widge = widge;
	}
	
	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb,
			float width, float height, String tagId, String textAlign,
			String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			buildHtml(widgeType, sb, width, height, tagId, textAlign, fontStyle);
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
			buildHtml(widgeType, sb, width, height, tagId, textAlign, fontStyle);
			return WIDGE_NAME;
		}else{
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
						textAlign, fontStyle);
		}
	}

	private void buildHtml(String widgeType, StringBuilder sb, float width,float height,
			String tagId, String textAlign, String fontStyle)
			throws FrameException {
		if (!validate(widgeType)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", WIDGE_NAME);
			throw new FrameException("3000000000000057", map);
		}
		
		Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
		String min = attrs.get("min")!= null?attrs.get("min").toLowerCase():"";//最小值
		String max = attrs.get("max")!= null?attrs.get("max").toLowerCase():"";//最大值
		String text = attrs.get("text")!= null?attrs.get("text"):"";//显示值
		
		int length = 0;
		sb.append(String
				.format(
						"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
						"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
						"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' min='%s' max='%s' size='%s'>%s</div></div>",
						width, height, tagId, tagId, widgeType, width, height, textAlign, fontStyle, min, max, length, text));
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
