package com.dotoyo.ims.dsform.allin;

/*
 * 沉降位移时间 
 */
public class DsplTimeWidge extends DateWidge {
	public String WIDGE_NAME = "dsplTime".toLowerCase();
	
	public DsplTimeWidge(IWidgeType widge) {
		this(widge,"dsplTime".toLowerCase());
	}
		
	public DsplTimeWidge(IWidgeType widge,String WIDGE_NAME) {
		super(widge,WIDGE_NAME);
	}

	@Override
	public boolean validate(String content){
		return true;
	}

	@Override
	public boolean isCurWid(String widgeType){
		if(widgeType.toLowerCase().startsWith(WIDGE_NAME)){
			return true;
		}
		return false;
	}
}
