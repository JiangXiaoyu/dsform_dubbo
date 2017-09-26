package com.dotoyo.ims.dsform.allin;
import com.dotoyo.dsform.model.AbstractModel;
public class UserCpyModel extends AbstractModel{
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private String userid;

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid == null ? null : userid.trim();
	}

	private String cpyid;

	public String getCpyid() {
		return this.cpyid;
	}

	public void setCpyid(String cpyid) {
		this.cpyid = cpyid == null ? null : cpyid.trim();
	}

}
