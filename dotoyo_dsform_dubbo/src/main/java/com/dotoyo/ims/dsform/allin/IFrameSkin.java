
package com.dotoyo.ims.dsform.allin;

/**
 * 框架换肤实现类
 * 
 * @author xieshh
 * 
 */
public interface IFrameSkin extends IFrame {
	public static final String[] skins={"aqua","gray","silvery"};
	public static final String SKIN_AQUA=skins[0];
	public static final String SKIN_GRAY=skins[1];
	public static final String SKIN_SILVERY=skins[2];
	/**
	 * 取默认的肤色信息
	 * @return
	 * @throws Exception
	 */
	public String getCurrentSkin() throws Exception;

	/**
	 * 设置肤色信息
	 */
	public void setCurrentSkin(String code) throws Exception;
}
