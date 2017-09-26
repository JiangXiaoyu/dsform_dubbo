package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;

public class SpringFrameService extends AbstractFrame implements IFrameService {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SpringFrameService.class));

	protected static SpringFrameService instance = null;


	protected SpringFrameService(Map<String, String> param) {

	}

	public static SpringFrameService getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return (SpringFrameService) instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new SpringFrameService(param);
		}
	}

	public ApplicationContext springContext = null;

	/**
	 * ������֪�����õķ�����ȡ����
	 * 
	 * @param svrCode
	 * @return
	 */
	public Object getService(String svrCode) throws Exception {
		if (StringsUtils.isEmpty(svrCode)) {
			return null;
		}

		if (springContext == null) {
			setApplicationContext();
		}
		//
		if (springContext != null) {

			return springContext.getBean(svrCode);
		}
		return null;

	}

	synchronized private void setApplicationContext() {

		if (springContext == null) {
			springContext = new ClassPathXmlApplicationContext(new String[] {
					"springs/applicationContext*.xml",
					"springs/*/applicationContext*.xml",
					"springs/*/*/applicationContext*.xml",
					"springs/*/*/*/applicationContext*.xml" });
		}
	
	}

	/**
	 * ���ڰ��淶����ķ���ͨ���������ȡҵ��:���ר��
	 * 
	 * @param svrCode
	 * @return
	 * @throws Exception
	 */
	public Object getService(Class<?> cls) throws Exception {
		if (cls == null) {
			return null;
		}
		String svrCode = cls.getSimpleName();
		if (svrCode.length() <= 1 || StringsUtils.isEmpty(svrCode)) {
			return null;
		}

		byte bytes[] = svrCode.getBytes();
		bytes[1] = new String(bytes, 1, 1).toLowerCase().getBytes()[0];
		return getService(String.format("frame.%s", new String(bytes, 1,
				bytes.length - 1)));

	}

	public void startup() {

	}

	public void shutdown() {
		instance = null;

	}

}
