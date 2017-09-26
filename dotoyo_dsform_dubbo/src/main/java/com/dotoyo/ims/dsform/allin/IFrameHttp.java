package com.dotoyo.ims.dsform.allin;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * Http通迅模块
 * 
 * @author xieshhs
 * 
 */
public interface IFrameHttp {
	public String postJson(HttpBo bo, Map<String, String> head, String json)
			throws Exception;

	public String postXml(HttpBo bo, Map<String, List<String>> headList, String xml)
	throws Exception;
	
	// 以get方式获得数据
	public String getData(HttpBo bo) throws Exception;

	public String postInputStream(HttpBo bo, Cookie cs[],
			Map<String, List<String>> headList, InputStream is)
			throws Exception;

	public String postParam(HttpBo bo, Cookie cs[],
			Map<String, List<String>> headList, Map<String, String[]> paramList)
			throws Exception;

	public String postJson(HttpBo bo, Cookie cs[],
			Map<String, List<String>> headList, byte bytes[]) throws Exception;
}
