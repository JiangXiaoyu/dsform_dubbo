package com.dotoyo.dsform.excel;

public class ExcelTableTdBo {
	private int colNum = -1;
	private String width = "";
	private String height="";//控件的高度,不是td的高度
	private String colspan = "";
	private String rowspan = "1";
	private String align = "";    //allalign  align='left' valign='bottom'
	private String textAlign = "";//textAlign   left center right
	private String verAlign = "";//verticalAlign   bottom center top
	private int fs=0;
	private boolean editor = false;//是否是编辑器，如果包含就把 align 去掉
	private String content = "";
	private ExcelTableTdStyleBorderBo bo = new ExcelTableTdStyleBorderBo();

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public ExcelTableTdStyleBorderBo getBo() {
		return bo;
	}

	public void setBo(ExcelTableTdStyleBorderBo bo) {
		this.bo = bo;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	public boolean getEditor() {
		return editor;
	}

	public void setEditor(boolean isEditor) {
		this.editor = isEditor;
	}

	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public String getVerAlign() {
		return verAlign;
	}

	public void setVerAlign(String verAlign) {
		this.verAlign = verAlign;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
