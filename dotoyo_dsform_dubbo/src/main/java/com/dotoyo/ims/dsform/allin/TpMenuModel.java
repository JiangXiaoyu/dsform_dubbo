package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpMenuModel extends AbstractModel{
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

	private String url;

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	private String appId;

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId == null ? null : appId.trim();
	}

	private String dis_appId;

	public String getDis_appId() {
		return this.dis_appId;
	}

	public void setDis_appId(String dis_appId) {
		this.dis_appId = dis_appId == null ? null : dis_appId.trim();
	}

	private String pid1;

	public String getPid1() {
		return this.pid1;
	}

	public void setPid1(String pid1) {
		this.pid1 = pid1 == null ? null : pid1.trim();
	}

	private String pid2;

	public String getPid2() {
		return this.pid2;
	}

	public void setPid2(String pid2) {
		this.pid2 = pid2 == null ? null : pid2.trim();
	}

	private String pid3;

	public String getPid3() {
		return this.pid3;
	}

	public void setPid3(String pid3) {
		this.pid3 = pid3 == null ? null : pid3.trim();
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	private String pid4;

	public String getPid4() {
		return this.pid4;
	}

	public void setPid4(String pid4) {
		this.pid4 = pid4 == null ? null : pid4.trim();
	}

}
