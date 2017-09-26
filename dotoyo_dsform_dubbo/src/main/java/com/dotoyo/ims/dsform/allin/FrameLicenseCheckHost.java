package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;


/**
 * 
 * 
 * @author xieshh
 * 
 */
public class FrameLicenseCheckHost extends AbstractFrameLicenseCheck {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameLicenseCheckHost.class));

	public void check(LicenceBo bo) throws Exception {
		String homeName = FrameFactory.getHostName();
		Map<String, String> map = getMapBy(bo.getMac());
		if (!map.containsKey(homeName)) {
			// 3000000000000019=您的软件使用许可签名失败
			FrameException e = new FrameException("3000000000000019",
					new HashMap<String, String>());
			throw e;
		}
	}

	/**
	 * 取出所有[授权目标]集合,以[,]拆分每一项
	 * 
	 * @param hosts
	 * @return
	 * @throws Exception
	 */
	protected Map<String, String> getMapBy(String hosts) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if (StringsUtils.isEmpty(hosts)) {
			return map;
		}
		String hs[] = hosts.split(",");
		for (String h : hs) {
			if (StringsUtils.isEmpty(h)) {
				continue;
			}
			h = h.trim();
			map.put(h, h);
		}
		return map;
	}

}
