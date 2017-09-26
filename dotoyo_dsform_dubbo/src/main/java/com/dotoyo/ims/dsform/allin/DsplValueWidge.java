package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

/*
 *  沉降位移观察值 
 */
public class DsplValueWidge implements IWidgeType {
	public static final String WIDGE_NAME = "dsplValue".toLowerCase();
	private IWidgeType nextWidge = null;

	public DsplValueWidge(IWidgeType widge) {
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
			String time = attrs.get("time")!=null?attrs.get("time").toLowerCase():"";//表达式必须小写
			String no = attrs.get("no")!=null?attrs.get("no").toLowerCase():"";//表达式必须小写
			int length = 0;
			sb.append(String
					.format(//contenteditable='true' showmenu='true'必须在一起
							"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' time='%s' no='%s' oninput='calcDspl(this);' onkeyup='calcDspl(this);' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;background-color:%s;%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' validate='isNum' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, time, no, width, height, textAlign, PreformConstant.BACKGROUND_COLOR_3, fontStyle, length/2));
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String time = attrs.get("time")!=null?attrs.get("time").toLowerCase():"";//表达式必须小写
			String no = attrs.get("no")!=null?attrs.get("no").toLowerCase():"";//表达式必须小写
			int length = 0;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' time='%s' no='%s' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, time, no, width, height, textAlign, fontStyle, length/2));
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
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
