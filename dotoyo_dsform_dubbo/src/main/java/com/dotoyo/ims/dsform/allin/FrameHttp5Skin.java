
package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * http5下的换肤实现类
 * 
 * @author xieshh
 * 
 */
public class FrameHttp5Skin extends AbstractFrame implements IFrameSkin {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameHttp5Skin.class));

	protected static FrameHttp5Skin instance = null;

	protected FrameHttp5Skin() {

	}

	public static FrameHttp5Skin getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameHttp5Skin();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

	}

	public void startup() {
		

	}

	public void shutdown() {
		instance=null;
	}

	public String getCurrentSkin() throws Exception {

		
			return FrameConfig.getInstance().getConfig("skin");
		
		
	}
	public void setCurrentSkin(String skin) throws Exception {
		
			FrameConfig.getInstance().setConfig("skin", skin);
			
	}
	

	
}
