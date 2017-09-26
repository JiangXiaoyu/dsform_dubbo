package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知单的下拉控件
 * @author wangl
 *
 */
public class NoticeWidge implements IWidgeType {
	public static final String WIDGE_NAME = "notice".toLowerCase();
	private IWidgeType nextWidge = null;

	public NoticeWidge(IWidgeType widge) {
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
			sb.append(String.format("<div class='ptcp-div' style='width:%spx;'>",width));
			sb.append(String.format(
					"<select id='%ss' name='%ss' type='ptcp' input='%s' widgeType='%s' class='ptcp-select' onchange='selectChangeEvent(this);' style='padding:2px;width:%spx;'></select>",//这里padding必须为2,否则会影响样式
					tagId, tagId, tagId,WIDGE_NAME, width));//select的id与input的id不一样
			sb.append(String.format(
					"<input id='%s' name='%s' type='text' size='%s' class='ptcp-input' oninput='ptcpInputChange(this);' onpropertychange='ptcpInputChange(this);' widgeType='%s' style='padding:2px;width:%spx;%s'/>",//这里padding必须为2,否则会影响样式
					tagId, tagId, length, WIDGE_NAME, width-30, fontStyle));//减去30才能让下拉框显示出来
			sb.append("</div>");
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
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				String lenStr = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
				length = Integer.parseInt(lenStr);
			}
			width = width-2-2;//表格长度-padding-边框
			sb.append(String.format(
					"<span  id='%s' name='%s' type='text' widgeType='%s' style='padding:1px;width:%spx;text-align:%s;' size='%s'></span>",
					tagId, tagId, WIDGE_NAME, width, textAlign, length));
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
		if (widgeType.toLowerCase().equalsIgnoreCase(WIDGE_NAME.toLowerCase())){
			return true;
		}
		return false;
	}
}
