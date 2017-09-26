package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class FrameStaticData extends AbstractFrame implements IFrameStaticData {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameStaticData.class));

	protected static FrameStaticData instance = null;

	protected FrameStaticData() {

	}

	public static FrameStaticData getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new FrameStaticData();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public String getStaticData(String module, String key) throws Exception {
		IFrameService sF = FrameFactory.getServiceFactory(null);
		
		IStaticDataSv sv = (IStaticDataSv) sF.getService(IStaticDataSv.class);

		Map<String, Object> row=sv.getStaticData(module,key);
		return row==null?"":(String)row.get("VALUE");
	}

}
