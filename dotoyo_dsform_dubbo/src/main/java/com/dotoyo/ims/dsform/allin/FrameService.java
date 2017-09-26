
package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * 
 * @author xieshh
 * 
 */
public class FrameService extends AbstractFrame implements IFrameService {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameService.class));

	protected static FrameService instance = null;

	protected FrameService() {

	}

	public static FrameService getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameService();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	public Object getService(String svrCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getService(Class<?> cls) throws Exception {
		return null;
	}
}
