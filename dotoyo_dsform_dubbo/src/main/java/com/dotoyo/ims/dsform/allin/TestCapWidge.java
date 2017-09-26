package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

//检验批容量
public class TestCapWidge implements IWidgeType {
	private static final String WIDGE_NAME = "testCap".toLowerCase();
	private IWidgeType nextWidge = null;

	public TestCapWidge(IWidgeType widge) {
		this.nextWidge = widge;
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
			String title = "计量单位:平、平米、平方、平方米、m²、㎡、立方米、立方、方、m³、米、m、M。&#10;计数单位：个、件、种、套、部、间、吨、樘、扇、处、面、根、块、盏、条、台、只、幅、箱、束、按、榀。";
			int length = 0;
			Map<String,String> attrs = WidgeUtils.parseAttr(widgeType);
			String type = attrs.get("type")!=null ? attrs.get("type") : ""; //类型
			String unit = attrs.get("unit")!=null ? attrs.get("unit").toLowerCase() : ""; //单位
			String passRate = attrs.get("passrate")!=null ? attrs.get("passrate").toLowerCase() : ""; //合格率
			if(!"mult".equals(type)){
				sb.append(String
						.format(
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='true' showmenu='true' unit='%s' passrate='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;background-color:%s;%s' onfocus='toolBarFocusEvent(this);' onblur='toolBarBlurEvent(this);testFocusEvent(this);' title='%s' size='%s'></div></div>",
								width, height, tagId, tagId, WIDGE_NAME, unit, passRate, width, height, textAlign, PreformConstant.BACKGROUND_COLOR_3, fontStyle, title, length/2));
			}else{
				//检验批容量多样化
				String value = attrs.get("value")!=null ? attrs.get("value") : ""; //显示值
				String rule = attrs.get("rule")!=null ? attrs.get("rule") : ""; //规则
				String range = attrs.get("range")!=null ? attrs.get("range") : ""; //范围
				String exps = attrs.get("exps")!=null ? attrs.get("exps") : ""; //公式
				String min = attrs.get("min")!=null ? attrs.get("min") : ""; //最小值
				sb.append(String
						.format(
								"<div style='width: %spx; height: %spx; border: 1px solid #ccc;overflow: hidden;'>"+
								"<div id='%s' name='%s' widgeType='%s' isTextarea='true' contenteditable='false' showmenu='true' type='%s' value='%s' unit='%s' passrate='%s' rule='%s' range='%s' exps='%s' min='%s' style='width: %spx; height: %spx; display: table-cell;"+
								"float:left;text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;background-color:%s;%s' title='%s' size='%s'></div>"
								+ "<div style='width: 28px; height: %spx;line-height: %spx;display: table-cell;border: solid 1px #b7b7b7;background: #ededed; cursor: pointer;' ref='%s' onclick='multTestSelect(this);'>选"
								+ "</div>"
								+ "</div>",
								width, height, tagId, tagId, WIDGE_NAME, type, value, unit, passRate, rule, range, exps, min, width-28, height, textAlign, PreformConstant.BACKGROUND_COLOR_3, fontStyle, title, length/2, height,height,tagId));
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
				map.put("value", WIDGE_NAME);
				throw new FrameException("3000000000000057", map);
			}
			
			int length = 0;
			//vertical-align: middle;
			sb.append(String
					.format(
							"<div style='width: %spx; height: %spx; border: 0px solid #ccc;overflow: hidden;'>"+
							"<div id='%s' name='%s' widgeType='%s' isTextarea='false' contenteditable='false' showmenu='true' style='width: %spx; height: %spx; display: table-cell;"+
							"text-align:%s; vertical-align: middle; word-wrap: break-word; word-break: break-all;%s' size='%s'></div></div>",
							width, height, tagId, tagId, WIDGE_NAME, width, height, textAlign, fontStyle, length/2));
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
		if (widgeType.toLowerCase().startsWith(WIDGE_NAME.toLowerCase())){
			return true;
		}
		return false;
	}
}

