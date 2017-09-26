package com.dotoyo.dsform.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameMongodb;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.dsform.interf.ITfPreformService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SnUtils {
	
	public static String getOnlyIndex(String companyId, String projectId, String orgItemId, String preformId) {
		String onlyIndex = "";
		if(StringsUtils.isEmpty(projectId)){
			onlyIndex = companyId + '-' + '-' + preformId;
		}else{
			if(StringsUtils.isEmpty(orgItemId)){
				onlyIndex = companyId + '-' + projectId + '-' + preformId;
			}else{
				onlyIndex = companyId + '-' + projectId + '-' + orgItemId + '-' +  preformId;
			}
		}
		return onlyIndex;
	}
	
	/*
	 * 获得文件模版
	 */
	public static TfPreformModel getPreform(String preformId) throws Exception, FrameException {
		TfPreformModel preform = new TfPreformModel();
		IFrameService svF = FrameFactory.getServiceFactory(null);
		
		ITfPreformService sv = (ITfPreformService) svF.getService(TfPreformService.class.getName());
		preform.setId(preformId);
		preform = sv.selectByPrimaryKey(preform);

		if (preform == null) {
			throw new FrameException("3000000000000052",null);
		}
		return preform;
	}
	
	/*
	 *	验证参数的合法性，并返回文件模板id 
	 */
	public static String getPreformId(String _id, String companyId,
			String preformId) throws FrameException,
			Exception {
		Map<String, String> map = new HashMap<String,String>();
		if(StringsUtils.isEmpty(preformId)){
			IFrameMongodb mongodb = FrameFactory.getMongodbFactory(null);
			BasicDBObject dbObj = new BasicDBObject();
			dbObj.put("_id", new ObjectId(_id));
//			DBObject result = mongodb.findById("preformData", dbObj);//TODO
			DBObject result = new BasicDBObject();
			if (result == null) {
				throw new FrameException("3000000000000054",null);
			}
			preformId = result.get("id").toString();
			if(StringsUtils.isEmpty(preformId)){
				map.put("value", "preformId");
				throw new FrameException("3000000000000052",map);
			}
		}
		return preformId;
	}
	
	/*
	 * 获得年度后两位 
	 */
	public static String getCurYear() {
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		return year.substring(year.length() -2);
	}
	
	public static String getFullYear() {
		Calendar cal = Calendar.getInstance();
		String year = cal.get(Calendar.YEAR) + "";
		return year;
	}
	
	//流水号补码
	public static String snComplement(int cValue,int digit){
		String curValue = cValue + "";
		int curLeng = curValue.length();
		for(int i = 0; i < digit-curLeng; i++){
			curValue = "0" + curValue ;
		}
		return curValue;
	}
	
/*	public static void main(String[]args){
		
		SnUtils su = new SnUtils();
		System.out.println(su.snComplement(98, 2));
	}*/
}
