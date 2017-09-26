package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import com.dotoyo.dsform.excel.ExcelColumnBo;

/*
 * 单选控件
 */
public class CheckWidge implements IWidgeType {

	public static final String WIDGE_NAME = "chk".toLowerCase();

	private IWidgeType widge = null;
	
	private ExcelTableBo table = null;
	
	private ExcelTableTrBo tr = null;;
	
	private ExcelTableTdBo td = null;;

	public CheckWidge(IWidgeType widge,ExcelTableBo table, ExcelTableTrBo tr,
			ExcelTableTdBo td) {
		this.widge = widge;
		this.table = table;
		this.tr = tr;
		this.td = td;
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
			width = width-2-2;//表格长度-padding-边框
			
			String[] valueArray = null;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String value = attrs.get("value")!=null? attrs.get("value"):"";//属性值
			if("".equals(value)){//兼容以前的写法
				if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
					String values = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
					valueArray = values.split(",");
				}
			}else{
				valueArray = value.split(",");
			}
			sb.append("<div widgeType='chkDiv'>");
			String alias = tagId;//别名,不重复即可
			for(int i = 0; i < valueArray.length; i++ ){
				String v = valueArray[i];
				if(i != 0){
					tagId = getTagId(table);
				}
				doExcelColumnBo(tagId, table, tr, td,"TEXT",WIDGE_NAME);
				sb.append(String.format(
						"<input id='%s' name='%s' type='checkbox' alias='%s' enabled='enabled' widgeType='%s' style='padding:1px;%s' value='%s'/>%s",
						tagId, tagId, alias, WIDGE_NAME, fontStyle, v, v));
			}
			sb.append("</div>");
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
			width = width-2-2;//表格长度-padding-边框
			String[] valueArray = null;
			if(widgeType.indexOf("[") != -1 && widgeType.indexOf("]") != -1){
				String values = widgeType.substring(widgeType.indexOf("[")+1,widgeType.indexOf("]"));
				valueArray = values.split(",");
			}
			sb.append("<div widgeType='chkDiv'>");
			String alias = tagId;//别名,不重复即可
			for(int i = 0; i < valueArray.length; i++ ){
				String v = valueArray[i];
				if(i != 0){	
					tagId = getTagId(table);
				}
				doExcelColumnBo(tagId, table, tr, td,"TEXT",WIDGE_NAME);
				sb.append(String.format(
						"<input id='%s' name='%s' type='checkbox' alias='%s' disabled='disabled' widgeType='%s' style='padding:1px;%s' value='%s'/>%s",
						tagId, tagId, alias, WIDGE_NAME, fontStyle, v, v));
			}
			sb.append("</div>");
			return WIDGE_NAME;
		} else {
			if(widge == null){
				return "";
			}
			return widge.append2ViewHtml(widgeType, sb, width, height, tagId,
						textAlign, fontStyle);
		}
	}
	
	private void doExcelColumnBo(String tagId, ExcelTableBo table,
			ExcelTableTrBo tr, ExcelTableTdBo td,String type,String widgeType) {
		ExcelColumnBo bo = new ExcelColumnBo();
		bo.setCode(tagId);
		bo.setCol(String.format("C%s", table.getParam().size()));
		bo.setTabId(table.getParam().size()+"");
		bo.setThisType(type);
		bo.setValidType(widgeType);
		table.getParam().put(tagId, bo);
	}
	
	private String getTagId(ExcelTableBo table){
		int startIdValue = table.getStartIdValue();
		
		if(startIdValue == -1){
			return String.format("A%s", table.getParam().size());
		}else{
			return String.format("A%s", table.getParam().size() + startIdValue);
		}
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME + "[")){//必须是startWith
			return true;
		}
		return false;
	}
}
