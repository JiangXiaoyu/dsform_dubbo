package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

public class SubWidge implements IWidgeType {

	public String widgeName = "";
	public String content = "";
	private IWidgeType widge = null;

	public SubWidge(IWidgeType widge,String widgeName,String content) {
		this.widge = widge;
		this.widgeName = widgeName;
		this.content = content;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			buildHtml(widgeType, sb);
			return widgeName;
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
			buildHtml(widgeType, sb);
			return widgeName;
		}else{
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
						textAlign, fontStyle);
		}
	}
	
	private void buildHtml(String widgeType, StringBuilder sb) throws FrameException {
		if (!validate(widgeType)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", widgeName);
			throw new FrameException("3000000000000057", map);
		}
		
		//${sub[1111]}
		int startIndex = content.indexOf("${");
		int endIndex = content.indexOf("[");
		String dt = content.substring(startIndex+2, endIndex);
		startIndex = content.indexOf("[");
		endIndex = content.indexOf("]}");
		String value = content.substring(startIndex+1, endIndex);
		
		String temp = "";
		if("sub".equalsIgnoreCase(dt)){
			temp = "<sub>" + value  + "</sub>";
		}else if("sup".equalsIgnoreCase(dt)){
			temp = "<sup>" + value  + "</sup>";
		}
		content = content.replaceAll("\\$\\{" + dt + "\\[" + value + "\\]}" , temp);
		sb.append(content);
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if (widgeType.toLowerCase().startsWith(widgeName.toLowerCase() + "[")){
			return true;
		}
		return false;
	}
}
