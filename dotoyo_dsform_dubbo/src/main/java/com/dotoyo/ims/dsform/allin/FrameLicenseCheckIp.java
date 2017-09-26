package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * 
 * @author xieshh
 * 
 */
public class FrameLicenseCheckIp extends AbstractFrameLicenseCheck {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameLicenseCheckIp.class));

	public void check(LicenceBo bo) throws Exception {
		String homeName = FrameFactory.getIp();
		Map<String, String> map = getMapBy(bo.getMac());
		if (!map.containsKey(homeName)) {
			// 3000000000000019=您的软件使用许可签名失败
			FrameException e = new FrameException("3000000000000019",
					new HashMap<String, String>());
			throw e;
		}
	}

}
