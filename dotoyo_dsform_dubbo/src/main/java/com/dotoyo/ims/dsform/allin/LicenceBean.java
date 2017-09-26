package com.dotoyo.ims.dsform.allin;

import java.io.ByteArrayInputStream;
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
import com.dotoyo.dsform.util.StringsUtils;

public class LicenceBean {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(LicenceBean.class));

	public Map<String, String> getParam() {
		return param;
	}

	public LicenceBean(String xmlStr) throws Exception {
		if (StringsUtils.isEmpty(xmlStr)) {
			return;
		}
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(xmlStr.getBytes());

			load(is);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	// -------------------------------------------
	private final Map<String, String> param = new HashMap<String, String>();

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
			param.put(el.getName(), el.getTextTrim());
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
