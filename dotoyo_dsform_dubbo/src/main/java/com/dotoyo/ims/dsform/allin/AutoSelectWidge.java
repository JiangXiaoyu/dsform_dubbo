package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AutoSelectWidge implements IWidgeType {
	private IWidgeType nextWidge = null;

	public AutoSelectWidge(IWidgeType widge) {
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
			
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null?attrs.get("value"):"";
			String index = attrs.get("index")!=null?attrs.get("index"):"";
			String noColor = attrs.get("nocolor")!=null?attrs.get("nocolor"):""; //存在此属性表示隐藏背景色
			
			widgeType = getWidgeName(widgeType).toLowerCase();
			int length = 0;
			
			if(!"".equals(noColor)){
				sb.append(String
						.format(//contenteditable='true' showmenu='true'必须在一起
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' type='autoSelect' value='%s' index='%s' showColor='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' ondblclick='showAutoSlt(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s' title='双击选择'></div></div>",
								width, height, tagId, tagId, widgeType, value, index, noColor, width, height, textAlign, fontStyle, length/2));
			}else{
				sb.append(String
						.format(//contenteditable='true' showmenu='true'必须在一起
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' type='autoSelect' value='%s' index='%s' showColor='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;background-color:%s;%s' ondblclick='showAutoSlt(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s' title='双击选择'></div></div>",
								width, height, tagId, tagId, widgeType, value, index, noColor, width, height, textAlign, PreformConstant.BACKGROUND_COLOR_2, fontStyle, length/2));
			}
			return widgeType;
		} else {
			if(nextWidge == null){
				return "";
			}
			return nextWidge.append2EditHtml(widgeType, sb, width, height, tagId,
					textAlign, fontStyle);
		}
	}

	private String getWidgeName(String widgeType) {
		int index = widgeType.indexOf("[");
		if(index != -1){
			widgeType = widgeType.substring(0,index);
		}
		return widgeType;
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null?attrs.get("value"):"";
			String index = attrs.get("index")!=null?attrs.get("index"):"";
			String noColor = attrs.get("nocolor")!=null?attrs.get("nocolor"):""; //存在此属性表示隐藏背景色
			
			int length = 0;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' type='autoSelect' value='%s' index='%s' showColor='%s' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, widgeType, value, index, noColor, width, height, textAlign, fontStyle, length/2));
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
		Pattern pattern = Pattern.compile("slt\\d+[\\s\\S]*");
		if(pattern.matcher(widgeType.toLowerCase()).matches()){
			return true;
		}
		return false;
	}
}
