package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;


/**
 * 公共组件
 * @author wangl
 * 
 */
public class FramePublishComponent extends AbstractFrame implements IFramePublishComponent {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FramePublishComponent.class));

	protected static FramePublishComponent instance = null;

	protected FramePublishComponent() {

	}

	public static FramePublishComponent getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance(param);
		}
		return instance;
	}

	synchronized private static void newInstance(Map<String, String> param) {
		if (instance == null) {
			instance = new FramePublishComponent();
		}
	}


	public void startup() {
		// TODO Auto-generated method stub

	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
