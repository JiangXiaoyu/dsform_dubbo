package com.dotoyo.ims.dsform.allin;

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

/**
 * 加载ACTION配置
 * 
 * @author xieshh
 * 
 */
public class ActionConfig {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ActionConfig.class));
	protected Map<String, Object> paramMap = null;

	protected static ActionConfig instance = null;

	protected ActionConfig() throws Exception {
		String xmlPaths = FrameBean.getInstance().getConfig(
				String.format("%s.xmlPath", this.getClass().getName()));
		load(xmlPaths);

	}

	/**
	 * 从文件中加载
	 * 
	 * @param xmlFileName
	 * @throws DocumentException
	 * @throws IOException
	 * @throws FrameException
	 */
	public void load(String xmlPaths) throws Exception {
		paramMap = new HashMap<String, Object>(256);
		String[] list = xmlPaths.split(",");
		for (int i = 0; i < list.length; i++) {
			String xmlPath = list[i];
			load0(xmlPath);
		}
	}

	/**
	 * 从文件中加载
	 * 
	 * @param xmlFileName
	 * @throws DocumentException
	 * @throws IOException
	 * @throws FrameException
	 */
	private void load0(String xmlPath) throws Exception {

		InputStream is = null;
		try {
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
			load1(is);
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

	/**
	 * 从流中加载
	 * 
	 * @param xmlFileName
	 * @throws DocumentException
	 * @throws IOException
	 * @throws FrameException
	 */
	private void load1(InputStream is) throws Exception {
		// ------------
		
		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		for (Element el : list) {

			String name = el.getName();
			if (!"impl".equals(name)) {
				String code = el.attributeValue("code");
				String type = el.attributeValue("type");
				Map<String, Object> map = new HashMap<String, Object>(1024);
				map.put("type", type);
				paramMap.put(code, map);
				load2(el, map);
			} else {
				String code = el.getTextTrim();
				paramMap.put(name, code);
			}

		}

	}

	private void load2(Element el, Map<String, Object> paramMap)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Element> children = el.elements();
		for (Element child : children) {
			String name = child.getName();
			if (!"impl".equals(name)) {
				String code = child.attributeValue("code");
				Map<String, String> map = new HashMap<String, String>();
				paramMap.put(code, map);
				load3(child, map);
			} else {
				String code = child.getTextTrim();
				paramMap.put(name, code);
			}
		}

	}

	private void load3(Element el, Map<String, String> paramMap)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Element> children = el.elements();
		for (Element child : children) {
			String name = child.getName();
			if (!"impl".equals(name)) {
				String code = child.attributeValue("code");
				paramMap.put(code, child.getTextTrim());
			} else {
				String code = child.getTextTrim();
				paramMap.put(name, code);
			}
		}

	}

	public static ActionConfig getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new ActionConfig();
		}
	}

	public Map<String, Object> getActions() {
		return paramMap;
	}

	public static void main(String[] args) throws Exception {
		Object obj = ActionConfig.getInstance();
		log.debug(obj);
	}
}
