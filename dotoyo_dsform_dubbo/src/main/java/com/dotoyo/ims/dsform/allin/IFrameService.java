
package com.dotoyo.ims.dsform.allin;

public interface IFrameService extends IFrame {
	/**
	 * 根据业务编码取业务实现
	 * 
	 * @param svrCode
	 * @return
	 */
	public Object getService(String svrCode) throws Exception;
	/**
	 * 框架专用
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public Object getService(Class<?> cls) throws Exception;
}
