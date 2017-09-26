
package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

public class FrameArea extends AbstractFrame implements IFrameArea {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameArea.class));

	protected static FrameArea instance = null;

	protected FrameArea() {

	}

	public static FrameArea getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameArea();
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
