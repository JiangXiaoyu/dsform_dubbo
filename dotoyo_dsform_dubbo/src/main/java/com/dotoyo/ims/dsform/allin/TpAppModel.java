package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpAppModel extends AbstractModel{
//tpApp{{{{
//tpApp}}}}
//tpApp[[[[
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

	private String root;

	public String getRoot() {
		return this.root;
	}

	public void setRoot(String root) {
		this.root = root == null ? null : root.trim();
	}

	private String ip;

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	private String port;

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port == null ? null : port.trim();
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

//tpApp]]]]
}
