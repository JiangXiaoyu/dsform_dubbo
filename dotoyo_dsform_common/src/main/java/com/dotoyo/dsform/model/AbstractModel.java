package com.dotoyo.dsform.model;

abstract public class AbstractModel implements IModel {
	protected String tableSubName="";
	public void setTableSubName(String tableSubName){
		this.tableSubName=tableSubName;
	}
	public String getTableSubName(){
		return tableSubName;
	}
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1288916890858085850L;

	protected AbstractModel() {

	}

	
}
