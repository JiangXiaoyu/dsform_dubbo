package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 * 自动序号
 */
public class AutoSnWidge implements IWidgeType{
	
	public static final String WIDGE_NAME = "autosn".toLowerCase();

	private IWidgeType widge = null;

	public AutoSnWidge(IWidgeType widge) {
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
			int length = 0;
			//vertical-align: middle       
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							//此输入框已经为只读了,所以调整isTextarea='false' contenteditable='false'位置
							"<div id='%s' name='%s' isTextarea='false' contenteditable='false' widgeType='%s'  showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							" text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, width, height, textAlign, fontStyle, length/2));
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
			int length = 0;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							" text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, width, height, textAlign, fontStyle, length/2));
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
