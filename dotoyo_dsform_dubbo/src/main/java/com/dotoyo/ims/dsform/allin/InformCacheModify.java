package com.dotoyo.ims.dsform.allin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/*
 * 多节点部署时，通知缓存更新
 */
public class InformCacheModify {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(InformCacheModify.class));

	private static InformCacheModify instance = null;

	private List<String> clusterUrl = null;

	private InformCacheModify() throws Exception {
		init(FrameConfig.getInstance().getConfig("node"));
	}

	public static InformCacheModify getInstance(Map<String, String> param)
			throws Exception {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() throws Exception {
		if (instance == null) {
			instance = new InformCacheModify();
		}
	}

	protected void init(String value) throws Exception {
		if (value == null || value.equals("")) {
			clusterUrl = new ArrayList<String>();
			;
		} else {
			String[] result = value.split(",");
			clusterUrl = Arrays.asList(result);
		}
	}

	/**
	 * 通知其他节点同步缓存
	 * 
	 * @param daoClassName
	 * @throws Exception
	 */
	public void infomCacheModify(String daoClassName) throws Exception {
		IFrameHttp http = FrameFactory.getHttpFactory(null);
		for (String url : clusterUrl) {
			FrameFactory.getThreadFactory(null).execute("informCache",
					new CacheThread(url, http, daoClassName));
		}
	}
}
