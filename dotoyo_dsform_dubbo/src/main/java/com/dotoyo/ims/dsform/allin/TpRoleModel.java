package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpRoleModel extends AbstractModel{
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	private String state;

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	private String code;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	private Timestamp createTime;

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
