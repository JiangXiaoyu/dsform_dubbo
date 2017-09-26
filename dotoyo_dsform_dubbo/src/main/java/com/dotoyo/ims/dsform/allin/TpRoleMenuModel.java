package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpRoleMenuModel extends AbstractModel{
	//tpRoleMenu[[[[	
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String menuId;

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId == null ? null : menuId.trim();
	}

	private String dis_menuId;

	public String getDis_menuId() {
		return this.dis_menuId;
	}

	public void setDis_menuId(String dis_menuId) {
		this.dis_menuId = dis_menuId == null ? null : dis_menuId.trim();
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
	//tpRoleMenu]]]]
}
