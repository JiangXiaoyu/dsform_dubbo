package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

/**
 *  textarea--文本域
 */

public class TextareaWidge implements IWidgeType {

	public String WIDGE_NAME = "textarea".toLowerCase();

	private IWidgeType widget = null;
	
	private ExcelTableBo table = null;
	
	private ExcelTableTrBo tr = null;
	
	private ExcelTableTdBo td = null;
	
	public TextareaWidge(IWidgeType widget,ExcelTableBo table, ExcelTableTrBo tr, ExcelTableTdBo td) {
		this.widget = widget;
		this.table = table;
		this.tr = tr;
		this.td = td;
	}

	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException{
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			//宽度减去margin
			String widthStr = (width -2) + "px";// 减去2px表示border
			String heigthStr = ( height -2) + "px";//高度不用减太多
			String verAlign = td.getVerAlign();

			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			Set<String> keys = attrs.keySet();
			JSONArray jArray = JSONArray.fromObject(keys);
			String attrJson = jArray.size() > 0 ? jArray.toString() : "";
			
			//自适应高度不控制高度 与 字数
			if("auto".equals(table.getThisType())){
				sb.append(String
						.format(
								"<div style='width: %s; border: 1px solid #ccc;'>"+
								"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='true' showmenu='true' attr='%s' style='width: %s; display: table-cell;"+
								" text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' size='%s'></div></div>",
								widthStr, tagId, tagId, attrJson, widthStr, textAlign, verAlign, length/2));
			}else{
				sb.append(String
						.format(
								"<div style='width: %s; height: %s; border: 1px solid #ccc;'>"+
								"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='true' showmenu='true' attr='%s' style='width: %s; height: %s;display: table-cell;"+
								" text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' size='%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' oninput='changeEvent(this);' onpropertychange='changeEvent(this);'></div></div>",
								widthStr, heigthStr, tagId, tagId, attrJson, widthStr, heigthStr, textAlign, verAlign, length/2));
			}
			//控件id与excel行列关系
			try{
				table.getKeyValue().put(WidgeUtils.getColumnName(td.getColNum() + 1) + (tr.getRowNum()+1),  tagId);
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
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			//宽度减去margin
			String widthStr = ( width -2) + "px";//减去2px表示border
			String heigthStr = ( height - 2) + "px";//高度不用减太多
			String verAlign = td.getVerAlign();
			
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			Set<String> keys = attrs.keySet();
			JSONArray jArray = JSONArray.fromObject(keys);
			String attrJson = jArray.size() > 0 ? jArray.toString() : "";
			
			//resize:none;不能拖拽大小 outline:none;没有边框
			if("auto".equals(table.getThisType())){
				//自适应高度
				sb.append(String
						.format(
								"<div style='width: %s; heigth:%s; border: 0px solid #ccc;'>"+
								"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='false' showmenu='false' attr='%s' style='width: %s;  heigth:%s; %s; display: table-cell;"+
								"  text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' size='%s'></div></div>",
								 widthStr, heigthStr, tagId, tagId, attrJson, widthStr, heigthStr, fontStyle, textAlign, verAlign, length/2));
			}else{
				//固定高度限制
				sb.append(String
						.format(
								"<div style='width: %s; height: %s; overflow: hidden; border: 0px solid #ccc;'>"+
								"<div id='%s' name='%s' class='area-edit' isTextarea='true' contenteditable='false' showmenu='false' attr='%s' style='width: %s; height: %s; %s; display: table-cell;"+
								"  text-align:%s; vertical-align: %s; word-wrap: break-word; word-break: break-all;' size='%s'></div></div>",
								widthStr, heigthStr, tagId, tagId, attrJson, widthStr, heigthStr, fontStyle, textAlign, verAlign, length/2));//这里的高度是必须有的  
			}
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
