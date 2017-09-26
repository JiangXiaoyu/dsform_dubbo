package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 缓存刷新配置
 * 
 * @author xieshh
 * 
 */
public class CacheConfigBean {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(CacheConfigBean.class));

	protected static CacheConfigBean instance = null;
	private Properties param = new Properties();

	protected CacheConfigBean() throws Exception {
		InputStream is = null;
		String filePath = "";
		try {
			filePath = FrameBean.getInstance().getConfig(
					String.format("%s.filePath", this.getClass().getName()));
			is = this.getClass().getClassLoader().getResourceAsStream(filePath);
			if (is == null) {
				File file = new File(filePath);
				if (file.exists()) {
					is = new FileInputStream(file);
				} else {
					// 3000000000000025=根据路径${path}找不到配置文件
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

		//
		init(param);
	}

	public static CacheConfigBean getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new CacheConfigBean();
		}
	}

	// private static final Map<String, List<String>> table = new
	// HashMap<String, List<String>>();

	private final Map<String, Set<String>> daoCls = new HashMap<String, Set<String>>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// public static Set<String> getTable(String daoCls) {
	// return table.get(daoCls);
	// }

	protected void init(Properties param) throws Exception {
		for (Object t : param.keySet()) {
			String key = (String) t;
			if (StringsUtils.isEmpty(key)) {
				continue;
			}
			String value = (String) this.param.get(key);
			if (StringsUtils.isEmpty(value)) {
				continue;
			}
			Set<String> set = daoCls.get(key);
			if (set == null) {
				set = new HashSet<String>();
				set.add(value);
				daoCls.put(key, set);
			} else {
				set.add(value);
			}

		}
	}

	public Set<String> getDaocls(String table) {
		return daoCls.get(table);
	}

}
