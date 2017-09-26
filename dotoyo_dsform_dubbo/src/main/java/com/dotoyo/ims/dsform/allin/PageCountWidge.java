package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

public class PageCountWidge implements IWidgeType {

	public static final String WIDGE_NAME = "pagecount".toLowerCase();

	private IWidgeType widge = null;
	
	private String content = "";
	
	private String title = "打印时显示总页数";

	public PageCountWidge(IWidgeType widge,String content) {
		this.widge = widge;
		this.content = content;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			buildHtml(widgeType, sb, width, tagId, fontStyle);
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
	public String append2ViewHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			buildHtml(widgeType, sb, width, tagId, fontStyle);
			return WIDGE_NAME;
		}else{
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
						textAlign, fontStyle);
		}
	}

	private void buildHtml(String widgeType, StringBuilder sb, float width,
			String tagId, String fontStyle) throws FrameException {
		if (!validate(widgeType)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", WIDGE_NAME);
			throw new FrameException("3000000000000057", map);
		}
		String value = content.substring(content.indexOf("${")+2,content.indexOf("}"));
		content =  content.replaceAll( String.format("\\$\\{%s\\}" , value), "\\#");
		sb.append(String.format(
				"<span id='%s' name='%s' widgeType='pageCount' tdata='pageCount' style='overflow:hidden;width:%spx;%s'  title='%s'>%s</span>", tagId, tagId, width, fontStyle, title, content));
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
