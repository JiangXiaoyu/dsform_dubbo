package com.dotoyo.ims.dsform.allin;

public class PreformStringBuffer{
	private StringBuffer sb = null;
	private static final String NEW_LINE = "\r\n";
	
	public PreformStringBuffer(StringBuffer sb){
		this.sb = sb;
	}
	
	public void append(String str){
		sb.append(str);
		//##########################提交代码需要注释掉
		sb.append(NEW_LINE);
	}
	
	public StringBuffer getStringBuffer(){
		return sb;
	}
	
	public String toString(){
		return sb.toString();
	}
}
