package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * 随机数控件
 */
public class RndWidge implements IWidgeType{

	public static final String WIDGE_NAME = "rnd".toLowerCase();
	public static final String FIX_TYPE = "fix";
	public static final String CHK_TYPE = "chk";
	public static final String CAL_TYPE = "cal";
	public static final String COL_TYPE = "col";

	private IWidgeType widge = null;
	
	public RndWidge(IWidgeType widge) {
		this.widge = widge;
	}
	
	@Override
	public String append2EditHtml(String widgeType, StringBuilder sb,
			float width, float height, String tagId, String textAlign,
			String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String type = attrs.get("type")!=null?attrs.get("type"):"";
			
			String prec = attrs.get("prec")!=null?attrs.get("prec"):"";//精度
			String main = attrs.get("main")!=null?attrs.get("main"):"";//主控
			String uqrange = attrs.get("uqrange")!=null?attrs.get("uqrange"):"";//不合格范围
			int length = 0;
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			if(FIX_TYPE.equals(type)){
				String min = attrs.get("min");
				String max = attrs.get("max");
				sb.append(String.format(
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"//overflow: auto;防止溢出
										+ "<div id='%s' name='%s' widgeType='%s' isTextarea='true' rndType='%s' min='%s' max='%s' prec='%s' main='%s' uqrange='%s' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"
										+ " text-align:%s;word-wrap: break-word; word-break: break-all;%s' size='%s' oninput='rdFocusEvent(this,event);' onpropertychange='rdFocusEvent(this,event);' onkeyup='rdFocusEvent(this,event);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' title='随机数'></div></div>",
								width, height, tagId, tagId, WIDGE_NAME,
								type, min, max, prec, main, uqrange, width, height, textAlign,
								fontStyle, length / 2));
			}else if(CHK_TYPE.equals(type) || CAL_TYPE.equals(type)){
				String src = attrs.get("src") != null ? attrs.get("src").toLowerCase() : "";//excel行列
				sb.append(String.format(
						"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"//overflow: auto;防止溢出
								+ "<div id='%s' name='%s' widgeType='%s' isTextarea='true' rndType='%s' src='%s' prec='%s' main='%s' uqrange='%s' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"
								+ " text-align:%s;word-wrap: break-word; word-break: break-all;%s' size='%s' oninput='rdFocusEvent(this,event);' onpropertychange='rdFocusEvent(this,event);' onkeyup='rdFocusEvent(this,event);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' title='随机数'></div></div>",
						width, height, tagId, tagId, WIDGE_NAME, type, src, prec, main, uqrange,
						width, height, textAlign,fontStyle, length / 2));
			}else if(COL_TYPE.equals(type)){
				String src = attrs.get("src") != null ? attrs.get("src").toLowerCase() : "";//excel行列
				String rng = attrs.get("rng") != null ? attrs.get("rng").toLowerCase() : "";//excel行列
				sb.append(String.format(
						"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"//overflow: auto;防止溢出
								+ "<div id='%s' name='%s' widgeType='%s' isTextarea='true' rndType='%s' src='%s' rng='%s' prec='%s' main='%s' uqrange='%s' contenteditable='true' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"
								+ " text-align:%s;word-wrap: break-word; word-break: break-all;%s' size='%s' oninput='rdFocusEvent(this,event);' onpropertychange='rdFocusEvent(this,event);' onkeyup='rdFocusEvent(this,event);' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);' title='随机数'></div></div>",
						width, height, tagId, tagId, WIDGE_NAME, type, src, rng, prec, main, uqrange,
						width, height, textAlign,fontStyle, length / 2));
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
	public String append2ViewHtml(String widgeType, StringBuilder sb,
			float width, float height, String tagId, String textAlign,
			String fontStyle) throws FrameException {
		if (isCurWid(widgeType)) {
			if (!validate(widgeType)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			int length = 0;
			//overflow:hidden;display:inline-block;white-space:nowrap;  预览时不溢出
			sb.append(String.format(
				"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"//overflow: auto;防止溢出
				+ "<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='false' style='width: %spx; height: %spx; display: table-cell;"
				+ " text-align:%s; word-wrap: break-word; word-break: break-all;%s' size='%s' title='随机数'></div></div>",
				width, height, tagId, tagId,WIDGE_NAME, width, height, textAlign,fontStyle, length / 2));
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
	public boolean validate(String widgeType) {
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType) {
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
	
	public static void main(String[]args){
		String str = "${Rnd[type=fix][min=0][max=12]}";
		String[] arrs = str.split("[\\[\\]]");  //以[或]分割
		for(String s : arrs){
			System.out.println(s);
		}
		
		Map<String,String> map = WidgeUtils.parseAttr(str);
		for(Entry<String,String> entry :  map.entrySet()){
			System.out.println("key:" + entry.getKey() + ",val:" + entry.getValue());
		}
		
	}
}
