
package com.dotoyo.ims.dsform.allin;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * ACTION基础类
 * 
 * @author xieshh
 * 
 */
public class BaseAction {
	
	
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseAction.class));


	protected UserLoginBo getUserLoginBo() throws Exception {
		IFrameSession fs = FrameFactory.getSessionFactory(null);
		UserLoginBo lb = fs.getUserLoginBo();
		if(lb == null) lb = new UserLoginBo();
		lb.setUserId("ff8080814555142301455e47bf3000a3");
		return lb;
	}
		
	

	public static void main(String[] args) throws Exception {

	}
}
