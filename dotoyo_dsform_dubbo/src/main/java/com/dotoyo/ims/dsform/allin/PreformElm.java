package com.dotoyo.ims.dsform.allin;

import java.util.Date;

import com.dotoyo.dsform.model.AbstractModel;

public class PreformElm extends AbstractModel {

	private static final long serialVersionUID = 1509622490042362776L;

	private String id;//id
	private String widgeName;//控件name
	private String widgeId;//控件id
	private String widgeType;//控件类型
	private String style;//控件样式
	private String preformId;//preform id
	private String indexRow;//控件在第几行
	private String status;//状态
	private Date createTime;//创建时间
	private String width;//控件宽度
	private String height;//控件高度
	private String isRequired;//是否必填
	private String length;//控件输入长度
	private String align;//文字居左居右剧中

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidgeName() {
		return widgeName;
	}

	public void setWidgeName(String widgeName) {
		this.widgeName = widgeName;
	}

	public String getWidgeId() {
		return widgeId;
	}

	public void setWidgeId(String widgeId) {
		this.widgeId = widgeId;
	}

	public String getWidgeType() {
		return widgeType;
	}

	public void setWidgeType(String widgeType) {
		this.widgeType = widgeType;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getIndexRow() {
		return indexRow;
	}

	public void setIndexRow(String indexRow) {
		this.indexRow = indexRow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getPreformId() {
		return preformId;
	}

	public void setPreformId(String preformId) {
		this.preformId = preformId;
	}
}
