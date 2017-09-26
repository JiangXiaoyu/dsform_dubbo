package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;

public class PreformElmModel extends AbstractModel{
	
	private static final long serialVersionUID = 1509622490042362776L;
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String code;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	private String col;

	public String getCol() {
		return this.col;
	}

	public void setCol(String col) {
		this.col = col == null ? null : col.trim();
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	private String note;

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note == null ? null : note.trim();
	}

	private String thisType;

	public String getThisType() {
		return this.thisType;
	}

	public void setThisType(String thisType) {
		this.thisType = thisType == null ? null : thisType.trim();
	}

	private String formId;

	public String getFormId() {
		return this.formId;
	}

	public void setFormId(String formId) {
		this.formId = formId == null ? null : formId.trim();
	}

	private String isPk;

	public String getIsPk() {
		return this.isPk;
	}

	public void setIsPk(String isPk) {
		this.isPk = isPk == null ? null : isPk.trim();
	}

	private String isRequir;

	public String getIsRequir() {
		return this.isRequir;
	}

	public void setIsRequir(String isRequir) {
		this.isRequir = isRequir == null ? null : isRequir.trim();
	}

	private String validType;

	public String getValidType() {
		return this.validType;
	}

	public void setValidType(String validType) {
		this.validType = validType == null ? null : validType.trim();
	}

	private String tip;

	public String getTip() {
		return this.tip;
	}

	public void setTip(String tip) {
		this.tip = tip == null ? null : tip.trim();
	}

	private String isHiden;

	public String getIsHiden() {
		return this.isHiden;
	}

	public void setIsHiden(String isHiden) {
		this.isHiden = isHiden == null ? null : isHiden.trim();
	}

	private String isWrite;

	public String getIsWrite() {
		return this.isWrite;
	}

	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite == null ? null : isWrite.trim();
	}

	private String width;

	public String getWidth() {
		return this.width;
	}

	public void setWidth(String width) {
		this.width = width == null ? null : width.trim();
	}

	private String align;

	public String getAlign() {
		return this.align;
	}

	public void setAlign(String align) {
		this.align = align == null ? null : align.trim();
	}

	private String rowSpan;

	public String getRowSpan() {
		return this.rowSpan;
	}

	public void setRowSpan(String rowSpan) {
		this.rowSpan = rowSpan == null ? null : rowSpan.trim();
	}

	private String colSpan;

	public String getColSpan() {
		return this.colSpan;
	}

	public void setColSpan(String colSpan) {
		this.colSpan = colSpan == null ? null : colSpan.trim();
	}

	private String tabId;

	public String getTabId() {
		return this.tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId == null ? null : tabId.trim();
	}

	private String status;

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	private Timestamp createTime;

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
