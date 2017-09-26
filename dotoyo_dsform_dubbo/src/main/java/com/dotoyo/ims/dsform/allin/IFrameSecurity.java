package com.dotoyo.ims.dsform.allin;

/**
 * 安全模块:这是唯一的一个不是按组件化开发的实现类
 * 
 * @author xieshh
 * 
 */
public interface IFrameSecurity {

	public ClassLoader getClassLoader();

	/**
	 * 设置BTFRAME类加载器
	 * 
	 * @return
	 * @throws Exception
	 */
	public void setBtframeClassLoader();

	/**
	 * 清除BTFRAME类加载器
	 * 
	 * @return
	 * @throws Exception
	 */
	public void clearBtframeClassLoader();

	/**
	 * 生成软件使用许可
	 * 
	 * @param xmlStr
	 * @return
	 */
	public String buildLicense(String priKey, String xmlStr) throws Exception;

	/**
	 * 检查软件使用许可
	 * 
	 * @param xmlStr
	 * @return
	 */
	public boolean checkLicense(String xmlStr) throws Exception;
}
