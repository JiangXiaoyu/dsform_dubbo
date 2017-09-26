package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class ChineseLanguage extends AbstractFrame implements IFrameLanguage {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ChineseLanguage.class));

	private Properties pro = new Properties();

	private Map<String, String> map = new HashMap<String, String>(1000);

	protected static ChineseLanguage instance = null;
	// 项目模式
	private String mode = "";

	protected ChineseLanguage() {
		load();
	}

	/*
	 * 加载中文国际化，如果是调试模式，则将内容转码后载入内存
	 */
	private void load() {
		InputStream is = null;
		String filePath = "frame/language/zh_cn.properties";
		try {
			filePath = FrameBean.getInstance().getConfig(
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
			mode = FrameBean.getInstance().getConfig("mode");
		} catch (Throwable e) {
			log.error("", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {
					log.error("", e);
				}
			}
		}
		if ("debug".equals(mode)) {
			for (Object key : pro.keySet()) {
				String keyStr = (String) key;
				String value = pro.getProperty(keyStr);
				try {
					String encoding = "";
					try {
						encoding = FrameBean.getInstance()
								.getConfig("encoding");
					} catch (Throwable e) {
						encoding = "UTF-8";
					}

					value = new String(value.getBytes("iso-8859-1"), encoding);
				} catch (UnsupportedEncodingException e) {
					log.error("", e);
				}
				map.put(keyStr, value);
			}
		}
	}

	public static ChineseLanguage getInstance() {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new ChineseLanguage();
		}
	}

	public void startup() {

	}

	public void shutdown() {
		instance = null;

	}

	public String getWords(String code) {
		String value = "";
		if ("debug".equals(mode)) {
			value = map.get(code);
			if (value != null) {
				return value;
			}
		} else {
			value = pro.getProperty(code);
			if (value != null) {
				return value;
			}
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

	public static void main(String[] args) throws Exception {
		ChineseLanguage a = ChineseLanguage.getInstance();
		FileWriter fw = new FileWriter("a.xml");

		List<Map.Entry<String, String>> mappingList = new ArrayList<Map.Entry<String, String>>(
				a.map.entrySet());

		Collections.sort(mappingList,
				new Comparator<Map.Entry<String, String>>() {
					public int compare(Map.Entry<String, String> mapping1,
							Map.Entry<String, String> mapping2) {
						String val1 = mapping1.getKey();
						String val2 = mapping2.getKey();
						return val1.compareTo(val2);
					}
				});

		for (Object key : mappingList) {
			String k[] = (key + "").split("=");
			if(k.length!=2){
				continue;
			}
			fw.write("<lang code=\"" + k[0] + "\">" + k[1] + "</lang>");
		}
		fw.flush();
		fw.close();
	}
}
