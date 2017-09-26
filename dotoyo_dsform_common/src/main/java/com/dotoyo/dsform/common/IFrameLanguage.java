package com.dotoyo.dsform.common;

import java.util.Map;

/**
 * 软件国际化模块
 * 
 * @author xieshh
 * 
 */
public interface IFrameLanguage {
	public static final String[] types = { "zh_cn", "en", "zh_tw" };

	/**
	 * 把编码换为配置的字串
	 * 
	 * @param type
	 * @param param
	 */
	public String getWords(String code) throws Exception;

	/**
	 * 把编码换为配置的字串,处理占位符
	 * 
	 * @param type
	 * @param param
	 */
	public String getWords(String code, Map<String, String> param)
			throws Exception;

	/**
	 * 把编码换为配置的字串,处理占位符
	 * 
	 * @param code
	 * @param msg
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String getWords(String code, String msg, Map<String, String> param)
			throws Exception;

	/**
	 * 取得语言全局变量
	 * 
	 * @return
	 */
	public String getCode();

	/**
	 * 设置语言全局变量
	 */
	public void setCode(String code);
}
