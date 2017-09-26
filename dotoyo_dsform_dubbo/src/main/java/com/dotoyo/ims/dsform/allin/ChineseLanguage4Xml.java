package com.dotoyo.ims.dsform.allin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dotoyo.dsform.log.LogProxy;

public class ChineseLanguage4Xml extends AbstractFrame implements
		IFrameLanguage {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ChineseLanguage4Xml.class));

	private Properties pro = new Properties();

	// private Map<String, String> map = new HashMap<String, String>(1000);

	protected static ChineseLanguage4Xml instance = null;

	protected ChineseLanguage4Xml() {
		load();
	}

	/*
	 * 加载中文国际化，如果是调试模式，则将内容转码后载入内存
	 */
	private void load() {
		InputStream is = null;
		String filePath = "frame/language/zh_cn.xml";
		try {

			is = this.getClass().getClassLoader().getResourceAsStream(filePath);
			if (is == null) {
				File file = new File(filePath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					return;
				}
			}
			load(is);
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

	}

	/**
	 * 从流中加载
	 * 
	 * @param xmlFileName
	 * @throws DocumentException
	 * @throws IOException
	 * @throws FrameException
	 */
	private void load(InputStream is) throws Exception {
		// ------------

		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		for (Element el : list) {
			String code = el.attributeValue("code");
			pro.put(code, el.getTextTrim());

		}

	}

	public static ChineseLanguage4Xml getInstance() {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new ChineseLanguage4Xml();
		}
	}

	public void startup() {

	}

	public void shutdown() {
		instance = null;

	}

	public String getWords(String code) {
		String value = "";

		value = pro.getProperty(code);
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

	public static void main(String[] args) throws Exception {
		ChineseLanguage4Xml a = ChineseLanguage4Xml.getInstance();
		FileWriter fw = new FileWriter("b.xml");

		List<Map.Entry<Object, Object>> mappingList = new ArrayList<Map.Entry<Object, Object>>(
				a.pro.entrySet());

		Collections.sort(mappingList,
				new Comparator<Map.Entry<Object, Object>>() {
					public int compare(Map.Entry<Object, Object> mapping1,
							Map.Entry<Object, Object> mapping2) {
						String val1 = (String) mapping1.getKey();
						String val2 = (String) mapping2.getKey();
						return val1.compareTo(val2);
					}
				});

		Map map = new HashMap();
		BufferedReader fr = new BufferedReader(new InputStreamReader(
				new FileInputStream(
						"G:\\xsh\\dsframe\\config\\frame\\language\\a.txt"),
				"utf-8"));
		String line = fr.readLine();
		while (line != null) {
			String k[] = line.split("=");

			if (k.length == 2) {
				map.put(k[0], k[1]);
			}

			line = fr.readLine();
		}

		for (Object key : mappingList) {
			String k[] = (key + "").split("=");

			if (k.length != 2) {
				continue;
			}
			if (map.containsKey(k[0])) {
				fw.write("<lang code=\"" + k[0] + "\">" + map.get(k[0])
						+ "</lang>");
			} else {
				fw.write("<lang code=\"" + k[0] + "\">" + k[1] + "</lang>");
			}

		}
		fw.flush();
		fw.close();
	}
}
