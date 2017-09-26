package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

//最小抽样数
public class TestMinSampWidge implements IWidgeType {
	private static final String WIDGE_NAME = "testMinSamp".toLowerCase();
	private IWidgeType nextWidge = null;
	private String title = "";

	public TestMinSampWidge(IWidgeType widge, String title) {
		this.nextWidge = widge;
		this.title = title;
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String type = attrs.get("type") != null ? attrs.get("type").toLowerCase() : "";
			String rule = attrs.get("rule") != null ? attrs.get("rule").toLowerCase() : "";
			String range = attrs.get("range") != null ? attrs.get("range").toLowerCase() : "";
			String exps = attrs.get("exps") != null ? attrs.get("exps").toLowerCase() : "";
			String min = attrs.get("min") != null ? attrs.get("min").toLowerCase() : "";
			String main = attrs.get("main") != null ? attrs.get("main").toLowerCase() : "";
			String alias = attrs.get("alias") != null ? attrs.get("alias").toLowerCase() : "";
			
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' type='%s' rule='%s' range='%s' exps='%s' min='%s' main='%s' alias='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' onkeyup='toolBarFocusEvent(this);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' title='%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, type, rule, range, exps, min, main, alias, width, height, textAlign, fontStyle, title, length/2));
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String type = attrs.get("type") != null ? attrs.get("type").toLowerCase() : "";
			String rule = attrs.get("rule") != null ? attrs.get("rule").toLowerCase() : "";
			String range = attrs.get("range") != null ? attrs.get("range").toLowerCase() : "";
			String exps = attrs.get("exps") != null ? attrs.get("exps").toLowerCase() : "";
			String min = attrs.get("min") != null ? attrs.get("min").toLowerCase() : "";
			String main = attrs.get("main") != null ? attrs.get("main").toLowerCase() : "";
			String alias = attrs.get("alias") != null ? attrs.get("alias").toLowerCase() : "";
			
			//vertical-align: middle;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='true' type='%s' rule='%s' range='%s' exps='%s' min='%s' main='%s' alias='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, type, rule, range, exps, min, main, alias, width, height, textAlign, fontStyle, length/2));
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
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}

