package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpPostModel extends AbstractModel{
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

	private String state;

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	private Timestamp createTime;

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	private String userId;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	private String orgId;

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	private String dis_orgId;

	public String getDis_orgId() {
		return this.dis_orgId;
	}

	public void setDis_orgId(String dis_orgId) {
		this.dis_orgId = dis_orgId == null ? null : dis_orgId.trim();
	}

	private String roleId;

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId == null ? null : roleId.trim();
	}

	private String dis_roleId;

	public String getDis_roleId() {
		return this.dis_roleId;
	}

	public void setDis_roleId(String dis_roleId) {
		this.dis_roleId = dis_roleId == null ? null : dis_roleId.trim();
	}

}
