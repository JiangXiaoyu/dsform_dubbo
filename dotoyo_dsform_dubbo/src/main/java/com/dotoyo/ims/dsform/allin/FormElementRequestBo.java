
package com.dotoyo.ims.dsform.allin;

/**
 * 报表元素请求模型
 * 
 * @author xieshh
 * 
 */
public class FormElementRequestBo extends ReportElementRequestBo implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -745795913323548456L;
	private String formCode = "";
	private String itemCode = "";
	private String cls = "";
	private String isRequir = "false";
	private String isWrite = "true";
	private String isShowAll = "false";
	private String validType = "";
	private String style = "";
	private String tip = "";

	public String getIsWrite() {
		return isWrite;
	}

	public void setIsWrite(String isWrite) {
		this.isWrite = isWrite;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getValidType() {
		return validType;
	}

	public void setValidType(String validType) {
		this.validType = validType;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public String getIsRequir() {
		return isRequir;
	}

	public void setIsRequir(String isRequir) {
		this.isRequir = isRequir;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getIsShowAll() {
		return isShowAll;
	}

	public void setIsShowAll(String isShowAll) {
		this.isShowAll = isShowAll;
	}
}
