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
public class FrameSecurity extends AbstractFrame implements IFrameSecurity {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameSecurity.class));

	protected static FrameSecurity instance = null;

	protected FrameSecurity() {

	}

	public static FrameSecurity getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameSecurity();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

	public ClassLoader getClassLoader()  {

		return BtClassLoader.getInstance();
	}

	public void setBtframeClassLoader() {

		Thread.currentThread().setContextClassLoader(
				BtClassLoader.getInstance());

	}

	public void clearBtframeClassLoader() {

		Thread.currentThread().setContextClassLoader(
				BtClassLoader.getInstance().getParent());
	}
	public static void main(String[] args) {
		
		
	}

	public String buildLicense(String priKey,String xmlStr) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkLicense(String xmlStr) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
}
