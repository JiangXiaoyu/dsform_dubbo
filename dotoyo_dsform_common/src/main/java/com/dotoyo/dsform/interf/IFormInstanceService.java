package com.dotoyo.dsform.interf;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IFormInstanceService {
	
	public JSONObject getPreformData(String _id) throws Exception;
	
	public JSONObject savePreformData(JSONObject reqJson, JSONObject jsonObject);
	
	public JSONObject isFirstUsePreform(JSONObject reqJson) throws Exception;
	
	public JSONObject getShowSn(JSONObject reqJson);
	
	public JSONArray getFullSn(String _ids) throws Exception;
	
	public String getData(String ip);
	
//	public JSONObject loadPrintDirect(String id, String _id);
}
