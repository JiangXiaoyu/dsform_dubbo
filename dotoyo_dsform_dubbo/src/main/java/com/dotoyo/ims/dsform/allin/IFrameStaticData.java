
package com.dotoyo.ims.dsform.allin;

/**
 * 
 * @author xieshh
 * 
 */
public interface IFrameStaticData extends IFrame {
	/**
	 * 取有且只有一条记录配置
	 * 
	 * @param module
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getStaticData(String module, String code) throws Exception;
}
