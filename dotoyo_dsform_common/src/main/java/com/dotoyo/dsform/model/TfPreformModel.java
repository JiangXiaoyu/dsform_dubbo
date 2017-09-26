package com.dotoyo.dsform.model;
import java.sql.Timestamp;

public class TfPreformModel extends AbstractModel{
	private static final long serialVersionUID = 3371079256599948000L;
	
	private String id;

	private String name;

	private String note="normal";//上传方式  normal（一般创建）  batch(批量创建)  multipage(多页创建)

	private String thisType="fixed";//文件模版类型  fixed(固定高度 默认)  auto(自适应高度--已废弃)

	private String packages;

	private String method="new";//new(新表单首行有宽度) old（所有行都有宽度）

	private String title;

	private String status="1";//状态 （1 有效  0无效）

	private Timestamp createTime;//创建时间

	private String file;//上传文件的id （Excel  和 Zip格式）

	private String html;//可预览的HTML的附件id
	
	private String mdfHtml;//可编辑的HTML的附件id
	
	private String preformDataId;//存储在Mongodb上preformData的数据
	
	private String autoModelId;//自适应高度的 模版id
	
	private String mobileModelId;//手机模版id
	
	private String exampleId;//范例模板id
	
	private String fillInstrId;//填写说明模板id
	
	private String printDirection;//打印方向
	
	private String topMargin="1.0cm";//顶部留白,默认1.0cm
	
	private String leftMargin="2.0cm";//左边留白,默认2.0cm
	
	//===============================
	//目录规则
	//系统表单目录结构:	    /dtyims/resources/sys/单位类型/目录id/文件模版id
	//自定义表单-企业目录结构:	/dtyims/resources/custom/城市id/企业id/ent/文件模版id
	//自定义表单-机构目录结构:	/dtyims/resources/custom/城市id/企业id/机构id/文件模版id
	
	private String formType;//表单类型 ： 系统表单（sys） 自定义企业表单（cusEnt） 自定义机构表单（cusOrg）
	
	private String orgType;//单位类型
	
	private String rootId;//目录id
	
	private String cityId;//城市id
	
	private String entId;//企业id
	
	private String orgId;//机构id
	
	private Timestamp recreateDirecDate;//重建目录时间 (维护历史 重新目录的时间)
	
	private String virtualPath;//虚拟路径
	//===============================
	
	private String rebuildVersion;//重新编译版本
	
	private String snType;//流水号类型  sn(五位,超过五位后归0) yearsn(三位,超过三位或每过一年归0) cmsn(三位,超过三维后归0)  mulsn(多行,用户自己填)
	
	private String snStrFormat;//流水号格式化
	
	private String snInputId = "";//流水号input控件id
	
	private String totalOfSumTitle = "0";//sumTitle总数(分部分项汇总使用)
	
	private String totalOfSumSubSec = "0";//sumsubsec总数(分部分项汇总使用)
	
	private String itemNo;//年份单位工程分部子分部分项编号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getThisType() {
		return thisType;
	}

	public void setThisType(String thisType) {
		this.thisType = thisType;
	}

	public String getPackage() {
		return packages;
	}

	public void setPackage(String packages) {
		this.packages = packages;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getMdfHtml() {
		return mdfHtml;
	}

	public void setMdfHtml(String mdfHtml) {
		this.mdfHtml = mdfHtml;
	}

	public String getPreformDataId() {
		return preformDataId;
	}

	public void setPreformDataId(String preformDataId) {
		this.preformDataId = preformDataId;
	}

	public String getAutoModelId() {
		return autoModelId;
	}

	public void setAutoModelId(String autoModelId) {
		this.autoModelId = autoModelId;
	}

	public String getMobileModelId() {
		return mobileModelId;
	}

	public void setMobileModelId(String mobileModelId) {
		this.mobileModelId = mobileModelId;
	}

	public String getPrintDirection() {
		return printDirection;
	}

	public void setPrintDirection(String printDirection) {
		this.printDirection = printDirection;
	}

	public String getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(String topMargin) {
		this.topMargin = topMargin;
	}

	public String getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(String leftMargin) {
		this.leftMargin = leftMargin;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getEntId() {
		return entId;
	}

	public void setEntId(String entId) {
		this.entId = entId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Timestamp getRecreateDirecDate() {
		return recreateDirecDate;
	}

	public void setRecreateDirecDate(Timestamp recreateDirecDate) {
		this.recreateDirecDate = recreateDirecDate;
	}

	public String getRebuildVersion() {
		return rebuildVersion;
	}

	public void setRebuildVersion(String rebuildVersion) {
		this.rebuildVersion = rebuildVersion;
	}

	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}

	public String getSnType() {
		return snType;
	}

	public void setSnType(String snType) {
		this.snType = snType;
	}

	public String getSnStrFormat() {
		return snStrFormat;
	}

	public void setSnStrFormat(String snStrFormat) {
		this.snStrFormat = snStrFormat;
	}

	public String getSnInputId() {
		return snInputId;
	}

	public void setSnInputId(String snInputId) {
		this.snInputId = snInputId;
	}

	public String getTotalOfSumTitle() {
		return totalOfSumTitle;
	}

	public void setTotalOfSumTitle(String totalOfSumTitle) {
		this.totalOfSumTitle = totalOfSumTitle;
	}

	public String getTotalOfSumSubSec() {
		return totalOfSumSubSec;
	}

	public void setTotalOfSumSubSec(String totalOfSumSubSec) {
		this.totalOfSumSubSec = totalOfSumSubSec;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getExampleId() {
		return exampleId;
	}

	public void setExampleId(String exampleId) {
		this.exampleId = exampleId;
	}

	public String getFillInstrId() {
		return fillInstrId;
	}

	public void setFillInstrId(String fillInstrId) {
		this.fillInstrId = fillInstrId;
	}
}
