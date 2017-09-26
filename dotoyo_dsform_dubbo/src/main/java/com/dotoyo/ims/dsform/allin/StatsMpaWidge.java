package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

//统计MPA(试块组数,强度标准值,平均值,标准差,最小值,最大值,合格判定系数)
public class StatsMpaWidge implements IWidgeType {
	private String widgeName;
	private String title;
	private IWidgeType nextWidge = null;

	public StatsMpaWidge(IWidgeType widge,String widgeName,String title) {
		this.nextWidge = widge;
		this.widgeName = widgeName;
		this.title = title;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", widgeName);
				throw new FrameException("3000000000000057", map);
			}
			
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							//isTextarea='false' contenteditable='false'为false
							"<div id='%s' name='%s' isTextarea='false' contenteditable='false' widgeType='%s' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all; background-color:%s; %s' title='%s' size='%s'></div></div>",
							width, height, tagId, tagId, widgeName, width, height, textAlign, PreformConstant.BACKGROUND_COLOR_4, fontStyle, title, length/2));
			sb.append("</select>");
			return widgeName;
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
				map.put("value", widgeName);
				throw new FrameException("3000000000000057", map);
			}
			
			int length = 0;
			width = width-2-2;//表格长度-padding-边框
			//vertical-align: middle;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, widgeName, width, height, textAlign, fontStyle, length/2));
			return widgeName;
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
		if (widgeType.toLowerCase().equals(widgeName.toLowerCase())){
			return true;
		}
		return false;
	}
}

