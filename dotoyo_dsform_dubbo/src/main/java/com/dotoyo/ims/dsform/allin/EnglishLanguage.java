package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class EnglishLanguage extends AbstractFrame implements IFrameLanguage {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(EnglishLanguage.class));

	private Properties pro = new Properties();

	protected static EnglishLanguage instance = null;

	protected EnglishLanguage() {
		load();
	}

	private void load() {
		InputStream is = null;
		String filePath = "frame/language/en.properties";
		try {
			filePath= FrameBean.getInstance().getConfig(
					String.format("%s.filePath", this.getClass().getName()));
			is = this.getClass().getClassLoader().getResourceAsStream(filePath);
			if (is == null) {
				File file = new File(filePath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					return;
				}
			}
			pro.load(is);
		} catch (Throwable e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {

				}
			}
		}
	}

	public static EnglishLanguage getInstance() {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new EnglishLanguage();
		}
	}

	public void startup() {
		

	}

	public void shutdown() {
		instance=null;

	}

	public String getWords(String code) {
		String value = pro.getProperty(code);
		if (value != null) {
			return value;
		}
		return "";
	}

	public String getWords(String code, Map<String, String> param)
			throws Exception {
		String value = getWords(code);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (value.indexOf("${", i) != -1) {
			int prefixIndex = value.indexOf("${", i);
			int suffixIndex = value.indexOf("}", prefixIndex);
			if (suffixIndex == -1) {
				break;
			}
			sb.append(value.substring(i, prefixIndex));
			String placeholder = value.substring(prefixIndex + 2, suffixIndex);

			String v = param.get(placeholder);
			if (v != null) {
				sb.append(v);
			} else {
				sb.append("${" + placeholder + "}");
			}
			i = suffixIndex + 1;
		}
		sb.append(value.substring(i));
		return sb.toString();
	}

	public String getWords(String code, String msg, Map<String, String> param)
			throws Exception {
		String value = getWords(code);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (value.indexOf("${", i) != -1) {
			int prefixIndex = value.indexOf("${", i);
			int suffixIndex = value.indexOf("}", prefixIndex);
			if (suffixIndex == -1) {
				break;
			}
			sb.append(value.substring(i, prefixIndex));
			String placeholder = value.substring(prefixIndex + 2, suffixIndex);

			String v = param.get(placeholder);
			if (v != null) {
				sb.append(v);
			} else {
				sb.append(msg);
			}
			i = suffixIndex + 1;
		}
		sb.append(value.substring(i));
		return sb.toString();
	}

	public String getCode() {

		try {
			return FrameConfig.getInstance().getConfig("language");
		} catch (Throwable e) {
			log.error("", e);
		}
		return "";
	}

	public void setCode(String code) {
		try {
			FrameConfig.getInstance().setConfig("language", code);
		} catch (Throwable e) {
			log.error("", e);
		}

	}
}
