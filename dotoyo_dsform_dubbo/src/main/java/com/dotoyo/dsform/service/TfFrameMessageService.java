package com.dotoyo.dsform.service;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.dotoyo.dsform.dao.inter.IAnnexDao;
import com.dotoyo.dsform.interf.ITfFrameMessageService;
import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.MessageBo;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.FrameXss;
import com.dotoyo.ims.dsform.allin.IFrameMessage;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.IFrameXss;
import com.dotoyo.ims.dsform.allin.SecurityUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TfFrameMessageService implements ITfFrameMessageService {

	
	@SuppressWarnings("unchecked")
	public String getRespMessage(JSONObject json){
		String str = "";
		try {
			IFrameMessage msgF = FrameFactory.getMessageFactory(null);
			MessageBo bo = new MessageBo();
			bo.setRespType("json");
			str = msgF.getRespMessage(bo, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	public String getRespMessage(MessageBo bo, Throwable e) throws Exception {
		JSONObject retJson = getRespMessage4Json();
		if (e instanceof FrameException) {
			FrameException et = (FrameException) e;
			retJson.put("code", et.getCode());
		} else {
			retJson.put("code", "3000000000000000");
		}
		retJson.put("msg", e == null ? "" : e.getMessage());
		return retJson.toString();
	}
	
	public String getRespMessage(Throwable e) throws Exception {
		IFrameMessage msgF = FrameFactory.getMessageFactory(null);
		MessageBo bo = new MessageBo();
		bo.setRespType("json");
		@SuppressWarnings("unchecked")
		String str = msgF.getRespMessage(bo, e);
		
		return str;
	}
	
	protected JSONObject getRespMessage4Json() throws Exception {
		JSONObject ret = new JSONObject();
		ret.put("code", "0000000000000000");
		ret.put("msg", "成功");
		ret.put("body", "{}");
		return ret;
	}
	
	/**
	 * 事务提交
	 * 
	 * @param bo
	 * @throws Exception
	 */
	public void commit(AnnexModel bo) throws Exception {
		IFrameService svf = FrameFactory.getServiceFactory(null);
		IAnnexDao annexDao = (IAnnexDao) svf.getService(IAnnexDao.class
				.getName());
		bo.setState("3");
		annexDao.updateByPrimaryKey(bo);
	}
	
	public void fixJSONObject4Web(JSONObject json) throws Exception {
		for (Object key : json.keySet()) {
			Object obj = json.get(key);
			if (obj instanceof JSONObject) {
				JSONObject new_name1 = (JSONObject) obj;
				fixJSONObject4Web(new_name1);
			} else if (obj instanceof JSONArray) {
				JSONArray new_name1 = (JSONArray) obj;
				fixJSONArray4Web(new_name1);
			} else if (obj instanceof String) {
				String value = (String) obj;
				String str = URLDecoder.decode(URLDecoder
						.decode(value, "utf-8"), "utf-8");
				IFrameXss xss = FrameXss.getInstance(null);
				String encodeStr = xss.xss(str);
				json.put(key, encodeStr);
				if (SecurityUtil.doSqlValidate(str, false)) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("param", encodeStr);
					m.put("name", key + "");
					FrameException e = new FrameException("3000000000000028", m);
					throw e;
				}
			} else {

			}
		}
	}
	
	protected void fixJSONArray4Web(JSONArray array) throws Exception {
		Object[] a = array.toArray();
		for (int i = 0; i < a.length; i++) {
			Object obj = a[i];
			if (obj instanceof JSONObject) {
				JSONObject new_name1 = (JSONObject) obj;
				fixJSONObject4Web(new_name1);
			} else if (obj instanceof JSONArray) {
				JSONArray new_name1 = (JSONArray) obj;
				fixJSONArray4Web(new_name1);
			} else if (obj instanceof String) {
				String value = (String) obj;
				String str = URLDecoder.decode(URLDecoder
						.decode(value, "utf-8"), "utf-8");
				IFrameXss xss = FrameXss.getInstance(null);
				String encodeStr = xss.xss(str);
				a[i] = encodeStr;
				if (SecurityUtil.doSqlValidate(str, false)) {
					Map<String, String> m = new HashMap<String, String>();
					m.put("param", encodeStr);

					FrameException e = new FrameException("3000000000000028", m);
					throw e;
				}
			} else {

			}
		}
	}
	
	public String getRespMessage(JSONArray param)  {
		try {
			if (param instanceof JSONArray) {
				JSONArray array = (JSONArray) param;
				JSONObject ret = getRespMessage4Json();
				ret.put("body", array);
				return ret.toString();
			} else {
				// 未实现
				FrameException bte = new FrameException("3000000000000001", null);			
				throw bte;				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
