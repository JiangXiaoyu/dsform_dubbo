package com.dotoyo.ims.dsform.allin;

import com.dotoyo.dsform.model.AbstractModel;

public class ExcelHeader extends AbstractModel {

	private static final long serialVersionUID = -6262203927037027565L;
	
	private String headerType;// header footer
	
	private String leftContent = "";
	
	private String centerContent = "";
	
	private String rightContent = "";

	public String getHeaderType() {
		return headerType;
	}

	public void setHeaderType(String headerType) {
		this.headerType = headerType;
	}

	public String getLeftContent() {
		return leftContent;
	}

	public void setLeftContent(String leftContent) {
		this.leftContent = leftContent;
	}

	public String getCenterContent() {
		return centerContent;
	}

	public void setCenterContent(String centerContent) {
		this.centerContent = centerContent;
	}

	public String getRightContent() {
		return rightContent;
	}

	public void setRightContent(String rightContent) {
		this.rightContent = rightContent;
	}
	
	public String getContent(){
		if(!"".equals(leftContent)){
			return leftContent;
		}else if(!"".equals(centerContent)){
			return centerContent;
		}else if(!"".equals(rightContent)){
			return rightContent;
		}
		return "";
	}
	

}
