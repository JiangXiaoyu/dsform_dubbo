package com.dotoyo.dsform.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.dotoyo.dsform.interf.IFormInstanceService;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.dotoyo.dsform.service.inter.ISerialNumService;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.HttpBo;
import com.dotoyo.ims.dsform.allin.IFrameHttp;
import com.dotoyo.ims.dsform.allin.IFrameMongodb;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.IFrameServiceLog;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FormInstanceService implements  IFormInstanceService {
	
	
	@Autowired
    private MongoTemplate mongoTemplate;

	/**
	 * 获得指定预定义报表的填写的数据
	 * @param httpRequest
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public JSONObject getPreformData(String _id) throws Exception {
			JSONObject json = null; 
		try {			
//			IFrameServiceLog logF = FrameFactory.getServiceLogFactory(null);
//			logF.addServiceLog(IFrameServiceLog.QUERY, this.getClass(),
//					"getPreformData", reqJson.toString());
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ITfPreformService sv = (ITfPreformService) fs
					.getService(TfPreformService.class.getName());
			JSONObject reqJson = new JSONObject();
			reqJson.put("_id", _id);
			json = sv.getPreformData4Mongodb(reqJson);
			
		} catch (Exception e) {

		}
		return json;
	}
	
	public JSONObject savePreformData(JSONObject reqJson, JSONObject jsonObject) {
			JSONObject json = null;
		try {
			IFrameServiceLog logF = FrameFactory.getServiceLogFactory(null);
			logF.addServiceLog(IFrameServiceLog.INSERT, this.getClass(),
					"savePreformData", reqJson.toString());
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ITfPreformService sv = (ITfPreformService) fs
					.getService(TfPreformService.class.getName());
			json = sv.savePreformData4Mongodb(reqJson, jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return json;
	}
	
	public JSONObject isFirstUsePreform(JSONObject reqJson){
		JSONObject json = new JSONObject();
		try {
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ISerialNumService sv = (ISerialNumService) fs.getService(ISerialNumService.class.getName());
			boolean result = sv.isFirstUsePreform(reqJson);			
			json.put("result", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	public JSONObject getShowSn(JSONObject reqJson){
			JSONObject json = new JSONObject();
		try {
			IFrameService fs = FrameFactory.getServiceFactory(null);
			ISerialNumService sv = (ISerialNumService) fs
					.getService(ISerialNumService.class.getName());	
			json = sv.getShowSn(reqJson);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return json;
	}
	
	/**
	 * 根据指定实例_ids获取文件编号
	 * @param _ids 文件实例ids
	 * @return JSONArray 返回JSONArray  
	 * @throws Exception 
	 */
	public JSONArray getFullSn(String _ids) throws Exception {
		JSONArray  resultArray = new JSONArray();
		IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
		String[] _idArray = _ids.split(",");
		for(String _id : _idArray){
			BasicDBObject dbId = new BasicDBObject();
			dbId.put("_id", new ObjectId(_id));
//			DBObject resultdb = mongodb.findById("preformData", dbId);
			DBObject resultdb = null;//TODO
			if(resultdb != null){
				Object sn = resultdb.get("sn");
				JSONObject obj = new JSONObject();
				obj.put("_id", _id);
				obj.put("fullSn", sn);
				resultArray.add(obj);
			}
		}
		return resultArray;
	}
	
	public String getData(String ip){
		String weatherStr = "";
		try {
		IFrameHttp http = FrameFactory.getHttpFactory(null);		
		
		//根据ip获得位置信息
		HttpBo bo = new HttpBo();
		bo.setUrl("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip="+ ip);//通过sina接口查询ip的位置
		String locStr = http.getData(bo);
		JSONObject locRlt = JSONObject.fromObject(locStr);
		String localtion = locRlt.optString("city");
		
		//根据位置获得天气信息
		String ak = "D6DfqFbbvioS4CkGwaX9nR5n"; // baidu开发的ak
		bo.setUrl(String.format(
			"http://api.map.baidu.com/telematics/v3/weather?location=%s&output=json&ak=%s",localtion, ak));//通过百度查询当前位置的天气信息
		weatherStr = http.getData(bo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weatherStr;
	}
	
	
//	public JSONObject loadPrintDirect(String id, String _id){		
//		
//		JSONObject jObj = new JSONObject();
//		if(StringsUtils.isEmpty(id) || id.equals("undefined")){
//			
//			if(StringsUtils.isEmpty(_id) || _id.equals("undefined")){
//				return jObj;
//			}
//			BasicDBObject dbId = new BasicDBObject();
//			dbId.put("_id", new ObjectId(_id));
//			IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
//			DBObject result = mongodb.findById("preformData", dbId);
//			id = result.get("id").toString();
//		}
//		TfPreformModel record = new TfPreformModel();
//		record.setId(id);
//		record = dao.selectByPrimaryKey(record);
//		
//		if(record != null){
//			jObj.put("printDirec", record.getPrintDirection());//打印方向
//			jObj.put("note", record.getNote());//表单类型
//			jObj.put("topMargin", record.getTopMargin());
//			jObj.put("leftMargin", record.getLeftMargin());
//			jObj.put("snType", record.getSnType());//编号类型
//		}
//		return jObj;
//	}
}
