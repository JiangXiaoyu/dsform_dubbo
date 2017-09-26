
package com.dotoyo.ims.dsform.allin;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表请求模型
 * 
 * @author xieshh
 * 
 */
public class FormRequestBo extends ReportRequestBo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6764937715071182444L;

	private String formCode = "";

	private List<FormElementRequestBo> elList = new ArrayList<FormElementRequestBo>();;

	public List<FormElementRequestBo> getElList() {
		return elList;
	}

	public void setElList(List<FormElementRequestBo> elList) {
		this.elList = elList;
	}

	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

}
