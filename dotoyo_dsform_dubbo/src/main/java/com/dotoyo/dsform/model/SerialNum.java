package com.dotoyo.dsform.model;

import java.io.Serializable;
import java.util.Date;

import com.dotoyo.dsform.model.AbstractModel;

/**
 *  流水号
 */
public class SerialNum extends AbstractModel implements Serializable {

	private static final long serialVersionUID = 702579920968399189L;

	private String id;		 //流水号id
	
	private String companyId;//公司id
	
	private String projectId;//项目id
	
	private String preformId;//文件模版id
	
	private int curValue = 0;//从流水号1开始 (起始值为0，但流水号从1开始)
	
	private String snType;	 //流水号类型 sn(五位) yearsn(每过一年归0) cmsn(三位)
	
	private String onlyIndex;//公司id + 项目id + 文件模板  （此字段用于做唯一索引）
	
	private Date createDate; //创建时间
	
	private String year;//年份

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPreformId() {
		return preformId;
	}

	public void setPreformId(String preformId) {
		this.preformId = preformId;
	}

	public int getCurValue() {
		return curValue;
	}

	public void setCurValue(int curValue) {
		this.curValue = curValue;
	}

	public String getSnType() {
		return snType;
	}

	public void setSnType(String snType) {
		this.snType = snType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOnlyIndex() {
		return onlyIndex;
	}

	public void setOnlyIndex(String onlyIndex) {
		this.onlyIndex = onlyIndex;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
