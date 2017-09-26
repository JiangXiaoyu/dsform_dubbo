package com.dotoyo.dsform.excel;

public class ExcelTableTdStyleBorderBo {
	private String font = "";
	private String color = "";//背景色
	private String borderTop = "";
	private String borderLeft = "";
	private String borderBottom = "";
	private String borderRight = "";
	private String fontHeight;//字体大小
	private String fontName;//字体样式（宋体，黑体）
	private String fontItalic;//是否斜体
	private String fontColor;//字体颜色


	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(String borderTop) {
		this.borderTop = borderTop;
	}

	public String getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(String borderLeft) {
		this.borderLeft = borderLeft;
	}

	public String getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(String borderBottom) {
		this.borderBottom = borderBottom;
	}

	public String getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(String borderRight) {
		this.borderRight = borderRight;
	}

	public String getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(String fontHeight) {
		this.fontHeight = fontHeight;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public String getFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(String fontItalic) {
		this.fontItalic = fontItalic;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
}
