package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class AutoTextWidge implements IWidgeType {

	private IWidgeType widge = null;

	public AutoTextWidge(IWidgeType widge) {
		this.widge = widge;
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
			sb.append(String
					.format(//contenteditable='true' showmenu='true'必须在一起
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' type='autoText' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;background-color:%s;%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s' title='自动获取'></div></div>",
							width, height, tagId, tagId, widgeType.toLowerCase(), width, height, textAlign, PreformConstant.BACKGROUND_COLOR_1, fontStyle, length/2));
			
			return widgeType;
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
				map.put("value", widgeType);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='false' type='autoText' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s' ></div></div>",
							width, height, tagId, tagId, widgeType.toLowerCase(), width, height, textAlign,fontStyle,length/2));
			return widgeType;
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
		Pattern pattern = Pattern.compile("text\\d+");
		if(pattern.matcher(widgeType.toLowerCase()).matches()){
			return true;
		}
		return false;
	}
}
