package com.dotoyo.dsform.interf;

import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.MessageBo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface ITfFrameMessageService {

	public String getRespMessage(JSONObject json);
	
	public void commit(AnnexModel bo) throws Exception;
	
	public String getRespMessage(MessageBo bo, Throwable e) throws Exception;
	
	public String getRespMessage(Throwable e) throws Exception;
	
	public void fixJSONObject4Web(JSONObject json) throws Exception;
	
	public String getRespMessage(JSONArray jsonArray);
}
