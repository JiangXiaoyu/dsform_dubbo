package com.dotoyo.dsform.model;

public class MessageBo implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5934988849717279990L;
	private String reqCode;
	private String reqType;
	
	private String respCode;
	private String respType;

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getRespType() {
		return respType;
	}

	public void setRespType(String respType) {
		this.respType = respType;
	}

	

	public String getReqCode() {
		return reqCode;
	}

	public void setReqCode(String reqCode) {
		this.reqCode = reqCode;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

}
