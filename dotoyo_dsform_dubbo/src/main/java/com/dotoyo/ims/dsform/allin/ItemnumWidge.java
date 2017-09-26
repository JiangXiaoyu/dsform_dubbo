package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 分项工程数量
 * @author leim
 *
 */
public class ItemnumWidge implements IWidgeType {

	public static final String WIDGE_NAME = "itemnum".toLowerCase();

	private IWidgeType widge = null;

	public ItemnumWidge(IWidgeType widge) {
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

			int length = 0;
			if (widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1) {
				String lenStr = widgeType.substring(widgeType.indexOf("[") + 1,
						widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}

			// valign 默认 bottom
			sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
					"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
					"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
					width, height, tagId, tagId, widgeType,  width, height, textAlign, fontStyle, length/2));
			// orgPro 编辑转预览时用到
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
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}

			int length = 0;
			if (widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1) {
				String lenStr = widgeType.substring(widgeType.indexOf("[") + 1,
						widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}
			// vAlign 默认bottom
			sb
					.append(String
							.format(
									"<div style='width: %spx; height: %spx; border: 0px solid #ccc;'>"
											+ "<div id='%s' name='%s' widgeType='%s' class='%s' isTextarea='true' contenteditable='false' showmenu='false' style='width: %spx; height: %spx; display: table-cell;"
											+ " text-align:%s; vertical-align: bottom; word-wrap: break-word; word-break: break-all;%s' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' title='自动获取单位工程名称'></div></div>",
									width, height, tagId, tagId, widgeType,
									widgeType, width, height, textAlign,
									fontStyle, length / 2));
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
		Pattern pattern = Pattern.compile("itemnum\\d+");
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}else if(pattern.matcher(widgeType.toLowerCase()).matches() ){
				return true;
			}
		return false;
	}
}
