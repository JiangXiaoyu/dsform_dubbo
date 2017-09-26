package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 	${itemNo[val=1600010101]}
 *	年份+单位工程+分部+子分部+分项
 *  itemNo--年份单位工程分部子分部分项 编号
 */
public class ItemNoWidge implements IWidgeType {
	public static final String WIDGE_NAME = "itemNo".toLowerCase();
	private IWidgeType nextWidge = null;
	private ExcelTableBo table;

	public ItemNoWidge(IWidgeType widge,ExcelTableBo table) {
		this.nextWidge = widge;
		this.table = table;
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
			String val = attrs.get("val")!=null?attrs.get("val"):"";
			Pattern pattern = Pattern.compile("^\\d{10}$");
			if(pattern.matcher(val).matches()){
				table.setItemNo(val);
				sb.append(String
						.format(
								"<div id='%s' name='%s' widgeType='%s' val='%s' style='width: %spx; height: %spx;'></div>",
								tagId, tagId, WIDGE_NAME, val, width, height));
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
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String val = attrs.get("val")!=null?attrs.get("val"):"";
			Pattern pattern = Pattern.compile("^\\d{10}$");
			if(pattern.matcher(val).matches()){
				sb.append(String
						.format(
								"<div id='%s' name='%s' widgeType='%s' val='%s' style='width: %spx; height: %spx;'></div>",
								tagId, tagId, WIDGE_NAME, val, width, height));
			}
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
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME + "[")){
			return true;
		}
		return false;
	}
}
