package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpAccountModel extends AbstractModel{
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

	private String passwd;

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd == null ? null : passwd.trim();
	}

	private String dis_passwd;

	public String getDis_passwd() {
		return this.dis_passwd;
	}

	public void setDis_passwd(String dis_passwd) {
		this.dis_passwd = dis_passwd == null ? null : dis_passwd.trim();
	}

}
