package com.dotoyo.ims.dsform.allin;

/**
 * 许可检查实现类
 * 
 * @author xieshh
 * 
 */
public interface IFrameLicenseCheck {
	/**
	 * 失败抛出异常
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void check(LicenceBo bo) throws Exception;
}
