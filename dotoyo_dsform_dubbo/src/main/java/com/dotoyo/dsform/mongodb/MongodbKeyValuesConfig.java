package com.dotoyo.dsform.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.FrameBean;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.IFrameLog;

public class MongodbKeyValuesConfig {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MongodbKeyValuesConfig.class));

	protected Map<String, Map<String, String>> paramMap = new HashMap<String, Map<String, String>>();

	protected static MongodbKeyValuesConfig instance = null;

	protected MongodbKeyValuesConfig() throws Exception {
		String xmlPath = "";
		InputStream is = null;
		try {
			xmlPath = FrameBean.getInstance().getConfig(
					String.format("%s.xmlPath", this.getClass().getName()));
			is = getClass().getClassLoader().getResourceAsStream(xmlPath);
			if (is == null) {
				File file = new File(xmlPath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					// 3000000000000025=根据路径${path}找不到配置文件
					Map<String, String> m = new HashMap<String, String>();
					m.put("path", xmlPath);
					FrameException e = new FrameException("3000000000000025", m);
					throw e;
				}
			}
			load(is);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
		}

	}

	public static MongodbKeyValuesConfig getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new MongodbKeyValuesConfig();
		}
	}

	public Map<String, Map<String, String>> getKeyValues() {
		return paramMap;

	}

	public Map<String, String> getValue(String code) {
		return paramMap.get(code);

	}

	// -------------------------------------------

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
			Map<String, String> param = new HashMap<String, String>();
			paramMap.put(code, param);
			@SuppressWarnings("unchecked")
			List<Element> children = el.elements();
			for (Element child : children) {
				String key = child.getName();
				param.put(key, child.getTextTrim());
			}

		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		MongodbKeyValuesConfig.getInstance().getKeyValues();
	}

}
