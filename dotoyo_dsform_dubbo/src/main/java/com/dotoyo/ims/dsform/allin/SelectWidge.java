package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONArray;
/**
 *  下拉框--文本域
 */

public class SelectWidge implements IWidgeType {

	public String WIDGE_NAME = "select".toLowerCase();
	

	private IWidgeType widget = null;
	
	private ExcelTableBo table = null;
	
	private ExcelTableTrBo tr = null;
	
	private ExcelTableTdBo td = null;
	
	public SelectWidge(IWidgeType widget) {
		this.widget = widget;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException{
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", widgeType);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			Set<String> keys = attrs.keySet();
			JSONArray jArray = JSONArray.fromObject(keys);
			String attrJson = jArray.size() > 0 ? jArray.toString() : "";
			String attrJsonVal = "";
			if(StringUtils.isNotBlank(attrJson)){
				attrJsonVal = attrJson.substring(0,attrJson.length()-2)+"val\"]";
			}
			if (widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1) {
				WIDGE_NAME = widgeType.substring(0,widgeType.indexOf("["));
			}
			//<div attr='%s' id='attr_%s' style='display: none;'><div>
			sb.append(String.format(
					"<select id='%s' name='%s' widgeType='%s'  contenteditable='false' showmenu='true'  size='%s' onchange='onchangeSel(this)' style='padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;%s'>"
					+ "</select><div attr='%s' id='attr_%s' style='display: none;'></div><div attr='%s' id='attr_%sval' style='display: none;'></div>",
					tagId, tagId, WIDGE_NAME,length, width, textAlign, fontStyle,attrJson,tagId,attrJsonVal,tagId));
			//控件id与excel行列关系
			try{
				//table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
			}catch(Exception e){
				e.printStackTrace();
			}
			return WIDGE_NAME;
		} else {
			if(widget == null){
				return "";
			}
			return widget.append2EditHtml(widgeType, sb, width, height, tagId,
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
			Set<String> keys = attrs.keySet();
			JSONArray jArray = JSONArray.fromObject(keys);
			String attrJson = jArray.size() > 0 ? jArray.toString() : "";
			sb.append(String.format(
					"<span id='%s' name='%s' type='text' widgeType='%s' size='%s' style='overflow:hidden;display:inline-block;white-space:nowrap;padding:1px;width:%spx;text-align:%s;%s'></span>",
					tagId, tagId, WIDGE_NAME, length, width, textAlign, fontStyle));
//			sb.append(String.format(
//					"<select id='%s' name='%s' widgeType='%s' contenteditable='true' showmenu='true'  size='%s'   style='padding:1px;margin-bottom:3px;width:%spx;text-align:%s;border:1px solid #ccc;%s'>"
//					+ "<option value='%s'>%s</option></select>",
//					tagId, tagId, WIDGE_NAME, length,width, textAlign, fontStyle,attrJson,tagId));
//			sb.append("</select>");
			return WIDGE_NAME;
		} else {
			if(widget == null){
				return "";
			}
			return widget.append2ViewHtml(widgeType, sb, width, height, tagId,
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
