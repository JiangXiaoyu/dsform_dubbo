
package com.dotoyo.ims.dsform.allin;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表请求模型
 * 
 * @author xieshh
 * 
 */
public class ReportRequestBo implements java.io.Serializable {

	private String tagId = "";
	private String code = "";
	private String tiltle = "";
	private String cls = "";
	private String url = "";
	private String id = "";
	private String sort = "";
	private String order = "";

	public String getTiltle() {
		return tiltle;
	}

	public void setTiltle(String tiltle) {
		this.tiltle = tiltle;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<ReportElementRequestBo> getColumn1() {
		return column1;
	}

	public void setColumn1(List<ReportElementRequestBo> column1) {
		this.column1 = column1;
	}

	public List<ReportElementRequestBo> getColumn2() {
		return column2;
	}

	public void setColumn2(List<ReportElementRequestBo> column2) {
		this.column2 = column2;
	}

	public List<ReportElementRequestBo> getLockColumn() {
		return lockColumn;
	}

	public void setLockColumn(List<ReportElementRequestBo> lockColumn) {
		this.lockColumn = lockColumn;
	}

	private List<ReportElementRequestBo> column1 = new ArrayList<ReportElementRequestBo>();;
	private List<ReportElementRequestBo> column2 = new ArrayList<ReportElementRequestBo>();
	private List<ReportElementRequestBo> lockColumn = new ArrayList<ReportElementRequestBo>();

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

}
