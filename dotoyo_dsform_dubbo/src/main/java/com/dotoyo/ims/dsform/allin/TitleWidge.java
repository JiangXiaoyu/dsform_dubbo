package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;


/*
 * 标题控件，设置attr会自动带出
 * attr: unitn(单位工程) secn(分部) subsecn(子分部) itemn(分项) part(部位)
 * attr包含以下几种:text3|unit,sec,subsec,item,part
 */
public class TitleWidge implements IWidgeType {
	public static final String WIDGE_NAME = "title".toLowerCase();
	private IWidgeType nextWidge = null;

	public TitleWidge(IWidgeType widge) {
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
			String type = attrs.get("type")!=null?attrs.get("type").toLowerCase():"";//转为小写
			String attr = attrs.get("attr")!=null?attrs.get("attr").toLowerCase():"";//转为小写
			if("fix".equals(type) || "".equals(type) ){
				sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
					"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' value='%s' style='width: %spx; height: %spx; display: table-cell;"+
					"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; %s' size='%s'>%s</div></div>",
					width, height, tagId, tagId, WIDGE_NAME, value, width, height, textAlign, fontStyle, length/2, value));
			}else if("cust".equals(type) || !"".equals(attr)){
				sb.append(String.format(
					"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
					"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' value='%s' custom='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' style='width: %spx; height: %spx; display: table-cell;"+
					"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; %s' size='%s'></div></div>",
					width, height, tagId, tagId, WIDGE_NAME, value, attr, width, height, textAlign, fontStyle, length/2));				
			}
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
				map.put("value", widgeType);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null?attrs.get("value"):"";
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' value='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; %s' size='%s'>%s</div></div>",
							width, height, tagId, tagId, WIDGE_NAME, value, width, height, textAlign, fontStyle, length/2, value));
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
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
