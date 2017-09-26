package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

import net.sf.json.JSONObject;

public class CacheThread implements Runnable {

	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(CacheThread.class));

	private String url;

	private IFrameHttp http;

	private String daoClassName;

	public CacheThread(String url, IFrameHttp http, String daoClassName) {
		this.url = url;
		this.http = http;
		this.daoClassName = daoClassName;
	}

	public void run() {
		HttpBo bo = new HttpBo();
		Map<String, String> head = new HashMap<String, String>();
		url = url + "/cache";
		bo.setUrl(url);
		String json = "{'daoClsName':'" + daoClassName + "'}";
		try {
			json = http.postJson(bo, head, json);
		} catch (Exception e) {
			log.error("", e);
		}

		JSONObject jsonData = JSONObject.fromObject(json);
		String code = jsonData.getString("code");
		// 成功
		if (code != null && code.startsWith("0")) {
			log.debug(url + "更新缓存成功!");
		} else {
			log.debug(url + "更新缓存失败!");
		}
	}
}
