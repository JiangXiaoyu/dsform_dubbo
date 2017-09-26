
package com.dotoyo.ims.dsform.allin;

/**
 * 报表元素请求模型
 * 
 * @author xieshh
 * 
 */
public class ReportElementRequestBo implements java.io.Serializable {

	private String field = "id";
	private String title = "默认标题";
	private String width = "100";
	private String align = "center";
	private String rowspan = "1";
	private String colspan = "1";
	private String sortable = "false";
	private String idField ;
	private String lockable = "false";
	private String elType="text" ;

	public String getElType() {
		return elType;
	}

	public void setElType(String elType) {
		this.elType = elType;
	}

	

	public String getLockable() {
		return lockable;
	}

	public void setLockable(String lockable) {
		this.lockable = lockable;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getSortable() {
		return sortable;
	}

	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

}
