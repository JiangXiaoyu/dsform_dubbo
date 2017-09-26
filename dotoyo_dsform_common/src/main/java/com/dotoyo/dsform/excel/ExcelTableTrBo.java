package com.dotoyo.dsform.excel;

import java.util.HashMap;
import java.util.Map;

public class ExcelTableTrBo {
	private int rowNum = -1;
	private String height = "";
	private int lastColNum = -1;
	private boolean isFirstRowFlag = false;//首行标示（首行有宽度，其他行没有宽度）
	private Map<Integer, ExcelTableTdBo> tdMap = new HashMap<Integer, ExcelTableTdBo>();

	public Map<Integer, ExcelTableTdBo> getTdMap() {
		return tdMap;
	}

	public void setTdMap(Map<Integer, ExcelTableTdBo> tdMap) {
		this.tdMap = tdMap;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getLastColNum() {
		return lastColNum;
	}

	public void setLastColNum(int lastColNum) {
		this.lastColNum = lastColNum;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean isFirstRowFlag() {
		return isFirstRowFlag;
	}

	public void setFirstRowFlag(boolean isFirstRowFlag) {
		this.isFirstRowFlag = isFirstRowFlag;
	}
}
