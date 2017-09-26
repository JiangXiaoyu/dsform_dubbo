package com.dotoyo.ims.dsform.allin;
import java.sql.Timestamp;

import com.dotoyo.dsform.model.AbstractModel;
public class TpOrgModel extends AbstractModel{
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

	private String areaId;

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId == null ? null : areaId.trim();
	}

	private String pId1;

	public String getPId1() {
		return this.pId1;
	}

	public void setPId1(String pId1) {
		this.pId1 = pId1 == null ? null : pId1.trim();
	}

	private String pId2;

	public String getPId2() {
		return this.pId2;
	}

	public void setPId2(String pId2) {
		this.pId2 = pId2 == null ? null : pId2.trim();
	}

	private String pId3;

	public String getPId3() {
		return this.pId3;
	}

	public void setPId3(String pId3) {
		this.pId3 = pId3 == null ? null : pId3.trim();
	}

	private String pId4;

	public String getPId4() {
		return this.pId4;
	}

	public void setPId4(String pId4) {
		this.pId4 = pId4 == null ? null : pId4.trim();
	}

	private String pId5;

	public String getPId5() {
		return this.pId5;
	}

	public void setPId5(String pId5) {
		this.pId5 = pId5 == null ? null : pId5.trim();
	}

	private String pId6;

	public String getPId6() {
		return this.pId6;
	}

	public void setPId6(String pId6) {
		this.pId6 = pId6 == null ? null : pId6.trim();
	}

	private String pId7;

	public String getPId7() {
		return this.pId7;
	}

	public void setPId7(String pId7) {
		this.pId7 = pId7 == null ? null : pId7.trim();
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

}
