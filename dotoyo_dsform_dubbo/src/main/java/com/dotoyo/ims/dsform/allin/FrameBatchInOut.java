
package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class FrameBatchInOut extends AbstractFrame implements IFrameBatchInOut {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameBatchInOut.class));

	protected static FrameBatchInOut instance = null;

	protected FrameBatchInOut() {

	}

	public static FrameBatchInOut getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameBatchInOut();
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

	public AreaBean getAreaByCode(String code) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AreaBean> getAreaList(int start, int end) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AreaBean> getAreaChildList(int start, int end, String areaCode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AreaBean> getAreaAllChildList(int start, int end,
			String areaCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
