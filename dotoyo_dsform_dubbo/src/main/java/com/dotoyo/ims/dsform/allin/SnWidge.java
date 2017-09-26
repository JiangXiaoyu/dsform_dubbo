package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import com.dotoyo.dsform.util.StringsUtils;

public class SnWidge implements IWidgeType {

	public static final String WIDGE_NAME = "sn".toLowerCase();

	private IWidgeType widge = null;
	
	private ExcelTableBo table = null;

	public SnWidge(IWidgeType widge,ExcelTableBo table) {
		this.widge = widge;
		this.table = table;
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
			
			width = width-2-2;
			String snVal = "";
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				snVal = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
			}
			String snType = table.getSnType();
			
			if(snType.indexOf("|")==-1 && "|prefsn|brsn|brdatasn|brprojsn|secsn|".indexOf("|" + snType + "|") != -1){
				sb.append(String.format(
						"<input id='%s' name='%s' widgeType='snInput' type='%s' style='overflow:hidden;width:%spx;background-color:%s;%s' readonly='readonly'/>", tagId, tagId, snType, width, PreformConstant.BACKGROUND_COLOR_5, fontStyle));//这里不需要padding,因为很难看
				table.getSnInputList().add(tagId);
				table.setSnStrFormat(table.getSnStrFormat() + "%s");
			}else{
				if(!StringsUtils.isEmpty(snVal)){
					//snSpan表示编号中固定部分
					sb.append(String.format(
							"<span id='%s' name='%s' widgeType='snSpan' type='%s'>%s</span>", tagId, tagId, snType, snVal));
					table.setSnStrFormat(table.getSnStrFormat() + snVal);
				}else{
					//snInput表示编号中系统生成部分
					sb.append(String.format(
							"<span id='%s' name='%s' widgeType='snInput' type='%s' style='overflow:hidden;width:%spx;%s'></span>", tagId, tagId, snType, width ,fontStyle));//这里不需要padding,因为很难看
					table.getSnInputList().add(tagId);
					table.setSnStrFormat(table.getSnStrFormat() + "%s");
				}
			}
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
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			width = width-2-2;
			String snVal = "";
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				snVal = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
			}
			if(!StringsUtils.isEmpty(snVal)){
				sb.append(String.format(
						"<span id='%s' name='%s' widgeType='snSpan'>%s</span>", tagId, tagId, snVal));
			}else{
				sb.append(String.format(
						"<span id='%s' name='%s' widgeType='snInput' style='overflow:hidden;width:%spx;%s'></span>", tagId, tagId, width ,fontStyle));//这里不需要padding,因为很难看
			}
			return WIDGE_NAME;
		} else {
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
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
