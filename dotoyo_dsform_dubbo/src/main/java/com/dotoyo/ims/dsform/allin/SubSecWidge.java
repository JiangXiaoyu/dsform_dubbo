package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 *  subsection--子分部控件
 */
public class SubSecWidge implements IWidgeType {
	public static final String WIDGE_NAME = "subsec".toLowerCase();
	private IWidgeType nextWidge = null;

	public SubSecWidge(IWidgeType widge) {
		this.nextWidge = widge;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", widgeType);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null?attrs.get("value"):"";
			sb.append(String
					.format(//contenteditable='true' showmenu='true'必须在一起
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' value='%s' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s'></div></div>",
							width, height, tagId, tagId, widgeType, value, width, height, textAlign, fontStyle, length/2));
			return widgeType;
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
				map.put("value", widgeType);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null?attrs.get("value"):"";
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' value='%s' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, widgeType, value, width, height, textAlign, fontStyle, length/2));
			return widgeType;
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
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME) && !widgeType.toLowerCase().equals("subsecnum")){
			return true;
		}
		return false;
	}
}
