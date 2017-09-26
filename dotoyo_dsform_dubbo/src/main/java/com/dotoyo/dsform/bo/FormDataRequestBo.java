
package com.dotoyo.dsform.bo;

import java.util.Map;

import com.dotoyo.ims.dsform.allin.ReportDataRequestBo;

public class FormDataRequestBo extends ReportDataRequestBo implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6084663729019048838L;
	private Map<String, Object> row =null;

	public Map<String, Object> getRow() {
		return row;
	}

	public void setRow(Map<String, Object> row) {
		this.row = row;
	}
	
	
	
	
	
}
