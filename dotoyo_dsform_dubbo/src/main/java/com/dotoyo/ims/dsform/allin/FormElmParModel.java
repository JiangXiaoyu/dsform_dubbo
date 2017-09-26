package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class FormElmParModel extends AbstractModel{
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

	private String thisValue;

	public String getThisValue() {
		return this.thisValue;
	}

	public void setThisValue(String thisValue) {
		this.thisValue = thisValue == null ? null : thisValue.trim();
	}

	private String text;

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text == null ? null : text.trim();
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
