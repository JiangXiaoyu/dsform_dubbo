package com.dotoyo.ims.dsform.allin;

public interface IWidgeType {

	// 编辑页面
	public String append2EditHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException;

	// 预览页面
	public String append2ViewHtml(String widgeType, StringBuilder sb, float width,
			float height, String tagId, String textAlign, String fontStyle) throws FrameException;

	// 验证是否合法
	public boolean validate(String widgeType);

	// 判断是否是当前控件
	public boolean isCurWid(String widgeType);
}
