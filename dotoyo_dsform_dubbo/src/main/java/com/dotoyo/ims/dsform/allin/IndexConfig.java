package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * index配置文件
 * @author wangl
 */
public class IndexConfig {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(IndexConfig.class));
	private Properties param = new Properties();
	protected static IndexConfig instance = null;
	protected String filePath = "index/index.properties";
	
	public final static String INDEX_DIR1 = "indexDir1";//资源文件中的key
	public final static String INDEX_DIR2 = "indexDir2";//资源文件中的key

	protected IndexConfig() throws Exception {
		InputStream is = null;
		try {
			is = this.getClass().getClassLoader().getResourceAsStream(
			filePath);
			if (is == null) {
				File file = new File(filePath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					Map<String, String> m = new HashMap<String, String>();
					m.put("path", filePath);
					FrameException e = new FrameException(
							"3000000000000025", m);
					throw e;
				}
			}
			param.load(is);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Throwable e) {

				}
			}
		}
	}

	public static IndexConfig getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new IndexConfig();
		}
	}
	
	public String getConfig(String key) {
		return param.getProperty(key);
	}
}
