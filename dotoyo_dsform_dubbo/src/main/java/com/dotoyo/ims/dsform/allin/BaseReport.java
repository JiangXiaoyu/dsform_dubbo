
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
public class BaseReport extends AbstractFrame implements IFrameReport {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseReport.class));

	protected static BaseReport instance = null;

	protected BaseReport() {

	}

	public static BaseReport getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new BaseReport();
		}
	}

	public void startup() {

	}

	public void shutdown() {

	}

}
