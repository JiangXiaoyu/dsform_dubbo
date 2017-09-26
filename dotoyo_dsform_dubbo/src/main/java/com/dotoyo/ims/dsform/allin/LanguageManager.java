
package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 国际化管理器,默认加载frame/config.properties中language，也可以在安装界面设置
 * 
 * @author wangl
 * 
 */
public class LanguageManager {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(LanguageManager.class));
	private Properties pro = new Properties();
	private static LanguageManager instance = null;
	private String language = "";//zh_cn en
	
	private IFrameLanguage frameLanguage = null;
	
	private LanguageManager() {
		parseLanguage();
	}

	private void parseLanguage() {
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(

			"frame/config.properties");
			pro.load(is);
			language = pro.getProperty("language");
			setFrameLanguage(language);
		} catch (Exception e) {
			log.error("",e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {
					log.error("",e);
				}
			}
		}
	}

	public static LanguageManager getInstance() {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new LanguageManager();
		}
	}

	
	/**
	 * 安装包在设置语言时，调用此方法
	 * (默认中文)
	 * @param frameLanguage
	 */
	public IFrameLanguage setFrameLanguage(String language) {
		//FrameFactory.startup会传递一个空的map
		if(language == null && frameLanguage != null){
			return frameLanguage;
		}
		if("en".equals(language)){
			this.frameLanguage = EnglishLanguage.getInstance();
		}else{
			this.frameLanguage = ChineseLanguage4Xml.getInstance();
		}
		return this.frameLanguage;
	}
	
	public IFrameLanguage getFrameLanguage() {
		return frameLanguage;
	}
	
	/**
	 * 获得指定的语言模块
	 * @param param
	 * @return
	 */
	public IFrameLanguage getFrameLanguage(Map<String, String> param) {
		String value = "";
		
		if(param==null){
			value="zh_cn";
		}else{
			value=param.get("language");
		}
		return setFrameLanguage(value);
	}

	/**
	 * 业务类调用此方法获得value
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getWords(String code) throws Exception  {
	
		if(frameLanguage != null){
			return frameLanguage.getWords(code);
		}
		
		return "";
	}

	public String getLanguage() {
		return language;
	}
}
