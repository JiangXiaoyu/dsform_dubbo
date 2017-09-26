package com.dotoyo.ims.dsform.allin;
import java.util.Date;

import com.dotoyo.dsform.model.AbstractModel;
public class SysDirectoryModel extends AbstractModel{
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	private Date createDate;

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	private String createUserId;

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId == null ? null : createUserId.trim();
	}

	private Date updateDate;

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	private String updateUserId;

	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId == null ? null : updateUserId.trim();
	}

	private String version;

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	private String directoryType;

	public String getDirectoryType() {
		return this.directoryType;
	}

	public void setDirectoryType(String directoryType) {
		this.directoryType = directoryType == null ? null : directoryType.trim();
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	private String erOrder;

	public String getErOrder() {
		return this.erOrder;
	}

	public void setErOrder(String erOrder) {
		this.erOrder = erOrder == null ? null : erOrder.trim();
	}

	private String thisType;

	public String getThisType() {
		return this.thisType;
	}

	public void setThisType(String thisType) {
		this.thisType = thisType == null ? null : thisType.trim();
	}

	private String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value == null ? null : value.trim();
	}

	private String year;

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year == null ? null : year.trim();
	}

	private String city;

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city == null ? null : city.trim();
	}

	private String orgType;

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType == null ? null : orgType.trim();
	}

	private String parentId;

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId == null ? null : parentId.trim();
	}

	private String projectType;

	public String getProjectType() {
		return this.projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType == null ? null : projectType.trim();
	}

	private String province;

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

}
