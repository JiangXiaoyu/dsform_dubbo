package com.dotoyo.dsform.model;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class AnnexModel extends AbstractModel{
//annex{{{{
//annex}}}}
//annex[[[[
	private String id;
	
	private String code;

	private String name;
	
	private String path;
	
	private String busiId;
	
	private String state;
	
	private Timestamp createTime;
	
	private String thisType;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path == null ? null : path.trim();
	}

	public String getBusiId() {
		return this.busiId;
	}

	public void setBusiId(String busiId) {
		this.busiId = busiId == null ? null : busiId.trim();
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getThisType() {
		return this.thisType;
	}

	public void setThisType(String thisType) {
		this.thisType = thisType == null ? null : thisType.trim();
	}
//annex]]]]
}
