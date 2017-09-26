package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

public class RowColumnSpanBo {
	private Map<String, String> row = new HashMap<String, String>();

	private Map<String, String> col = new HashMap<String, String>();



	private Map<String, String> borderRigth = new HashMap<String, String>();
	private Map<String, String> borderBottom = new HashMap<String, String>();

	public Map<String, String> getRow() {
		return row;
	}

	public void setRow(Map<String, String> row) {
		this.row = row;
	}

	public Map<String, String> getCol() {
		return col;
	}

	public void setCol(Map<String, String> col) {
		this.col = col;
	}

	
	public Map<String, String> getBorderRigth() {
		return borderRigth;
	}

	public void setBorderRigth(Map<String, String> borderRigth) {
		this.borderRigth = borderRigth;
	}

	public Map<String, String> getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(Map<String, String> borderBottom) {
		this.borderBottom = borderBottom;
	}

}
