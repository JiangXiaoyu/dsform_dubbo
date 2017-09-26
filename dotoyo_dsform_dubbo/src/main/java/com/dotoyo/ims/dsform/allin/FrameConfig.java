package com.dotoyo.ims.dsform.allin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

/**
 * 不在安全模块的保障范围内的配置，注意一定要与实现类脱勾
 * 
 * @author xieshh
 * 
 */
public class FrameConfig {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameConfig.class));
	private Properties param = new Properties();
	protected Map<IFrame, String> frameInstance = new Hashtable<IFrame, String>();
	protected static FrameConfig instance = null;

	protected FrameConfig() throws Exception {
		String filePath = "";
		InputStream is = null;
		try {
			filePath = FrameBean.getInstance().getConfig(
					String.format("%s.filePath", this.getClass().getName()));
			is = this.getClass().getClassLoader().getResourceAsStream(

			filePath);
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

	}

	/**
	 * @deprecated
	 * @param param
	 * @param frameInstance
	 * @throws Exception
	 */
	protected void initFrame(Properties param, Map<IFrame, String> frameInstance)
			throws Exception {
		String startList = (String) param.get("startList");
		if (!StringsUtils.isEmpty(startList)) {
			List<String> list = StringsUtils.split(startList, ',');
			for (String clsName : list) {
				clsName = clsName.trim();
				Class<?> newInstance = this.getClass().getClassLoader()
						.loadClass(clsName);
				IFrame frame = (IFrame) getInstance(newInstance);
				frameInstance.put(frame, frame.getClass().getName());
			}
		}
	}

	private Object getInstance(Class<?> newInstance) throws Exception {
		try {

			Method method = newInstance.getMethod("getInstance", new Class[0]);

			return method.invoke(null, new Object[0]);
		} catch (InvocationTargetException e) {

			throw new Exception(e.getTargetException());

		}
	}

	public static FrameConfig getInstance() throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new FrameConfig();
		}
	}

	/**
	 * @deprecated
	 * @return
	 */
	public Map<IFrame, String> getFrameInstance() {
		return frameInstance;
	}

	/**
	 * 取框架配置
	 * 
	 * @return
	 */
	public void setConfig(String key, String value) {

		param.put(key, value);
	}

	/**
	 * 取框架配置
	 * 
	 * @return
	 */
	public String getConfig(String key) {
		return param.getProperty(key);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
