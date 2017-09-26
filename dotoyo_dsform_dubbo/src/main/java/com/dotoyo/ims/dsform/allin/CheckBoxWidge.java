package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;


public class CheckBoxWidge implements IWidgeType {
	private IWidgeType nextWidge = null;

	public CheckBoxWidge(IWidgeType widge) {
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
			String rt = attrs.get("rt")!=null?attrs.get("rt"):"";
			String index = attrs.get("index")!=null?attrs.get("index"):"";
			String noColor = attrs.get("nocolor")!=null?attrs.get("nocolor"):""; //存在此属性表示隐藏背景色
			
//			Set<String> keys = attrs.keySet();
//			JSONArray jArray = JSONArray.fromObject(keys);
			String attrJson ="";
			if(attrs.containsKey("rt")){
				attrJson = "[\"rt\"]";
			}
			
			widgeType = getWidgeName(widgeType).toLowerCase();
			int length = 0;
			
			if(!"".equals(noColor)){
				sb.append(String
						.format(//contenteditable='true' showmenu='true'必须在一起
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<input type='hidden' attr='%s' id='attr_%s'/>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true'  value='%s' index='%s' showColor='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' ondblclick='showAutoCheckBox(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s' title='双击选择'></div></div>",
								width, height,attrJson,tagId, tagId, tagId, widgeType,value, index, noColor, width, height, textAlign, fontStyle, length/2));
			}else{
				sb.append(String
						.format(//contenteditable='true' showmenu='true'必须在一起
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<input type='hidden' attr='%s' id='attr_%s'/>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true'   value='%s' index='%s' showColor='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' ondblclick='showAutoCheckBox(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' size='%s' title='双击选择'></div></div>",
								width, height,attrJson,tagId, tagId, tagId, widgeType, value, index, noColor, width, height, textAlign,fontStyle, length/2));
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
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' type='autoSelect' value='%s' index='%s'  showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, widgeType, value, index, width, height, textAlign, fontStyle, length/2));
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
		Pattern pattern = Pattern.compile("checkbox\\d+[\\s\\S]*");
		if(pattern.matcher(widgeType.toLowerCase()).matches()){
			return true;
		}
		return false;
	}
}
