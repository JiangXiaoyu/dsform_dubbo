package com.dotoyo.ims.dsform.allin;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 项目负责人
 * @author leim
 *
 */
public class LeaderWidge implements IWidgeType {
	private static final String WIDGE_NAME = "leader".toLowerCase();
	
	private IWidgeType widge = null;
	
	public LeaderWidge(IWidgeType widge) {
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
			if (widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1) {
				String lenStr = widgeType.substring(widgeType.indexOf("[") + 1,
						widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}

			// valign 默认 bottom
			//contenteditable div是否可编辑属性
			sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
					"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
					"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
					width, height, tagId, tagId, widgeType,  width, height, textAlign, fontStyle, length/2));
			// orgPro 编辑转预览时用到
			return widgeType;
/*			sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 1px solid #ccc;'>"
							+ "<div id='%s' name='%s' widgeType='%s'  isTextarea='true' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"
							+ " text-align:%s; vertical-align: bottom; word-wrap: break-word; word-break: break-all;background-color:%s;' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' title='自动获取企业名称'></div></div>",
							width, height, tagId, tagId, widgeType,width, height, textAlign, fontStyle, length / 2));
			// orgPro 编辑转预览时用到
			return widgeType;
*/		} else {
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
			if (widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1) {
				String lenStr = widgeType.substring(widgeType.indexOf("[") + 1,
						widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}
			// vAlign 默认bottom
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' style='padding:1px;width:%spx;text-align:%s;%s' size='%s'></span>",
					tagId, tagId, widgeType, width,textAlign, fontStyle, length));
			return widgeType;
/*			sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 0px solid #ccc;'>"
							+ "<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='false' style='width: %spx; height: %spx; display: table-cell;"
							+ " text-align:%s; vertical-align: bottom; word-wrap: break-word; word-break: break-all;%s' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);' title='自动获取企业名称'></div></div>",
							width, height, tagId, tagId, widgeType, width, height, textAlign,fontStyle, length / 2));
			return widgeType;
*/		} else {
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
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
		
	}
}
