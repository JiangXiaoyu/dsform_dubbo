
package com.dotoyo.dsform.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dotoyo.dsform.dao.inter.ISerialNumDao;
import com.dotoyo.dsform.model.SerialNum;
import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.service.inter.ISerialNumService;
import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameSequence;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.SnEntity;

import net.sf.json.JSONObject;

public class SerialNumService extends AbstractService implements ISerialNumService {
	
	private ISerialNumDao dao;
	
	private ISerialNumDao getDao() throws Exception{
		IFrameService fsv = FrameFactory.getServiceFactory(null);
		ISerialNumDao dao = (ISerialNumDao)fsv.getService("com.dotoyo.dsform.dao.inter.ISerialNumDao");	
		return dao;
	}
	
	@Override
	public void insert(SerialNum sn) throws Exception {
		if(dao == null){
			dao = getDao();
		}
		dao.insert(sn);
	}

	@Override
	public SerialNum selectById4Lock(String id) throws Exception {
		if(dao == null){
			dao = getDao();
		}
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		return dao.selectById4Lock(paramMap);
	}

	@Override
	public void update(SerialNum sn) throws Exception {
		if(dao == null){
			dao = getDao();
		}
		dao.update(sn);
	}

	/*
	 * 获得展示用的流水号（可以出现重复）
	 */
	@Override
	public JSONObject getShowSn(JSONObject reqJson) throws  Exception {
		String companyId = reqJson.optString("companyId");
		String projectId = reqJson.optString("projectId");
		String orgItemId = reqJson.optString("orgItemId");
		String preformId = reqJson.optString("preformId");
		String _id = reqJson.optString("_id");
		preformId = SnUtils.getPreformId(_id, companyId, preformId);
		
		JSONObject jsonObj = new JSONObject();
		
		TfPreformModel pf = SnUtils.getPreform(preformId);
		String snType = pf.getSnType();
		jsonObj.put("snType", snType);
		if(StringsUtils.isEmpty(snType) || "mulsn".equals(snType) || "secsn".equals(snType)){
			return jsonObj;
		}
		if(StringsUtils.isEmpty(companyId)){
			Map<String, String> map = new HashMap<String,String>();
			map.put("value", "companyId");
			throw new FrameException("3000000000000052",map);
		}
		
		
		Map<String,String> map = new HashMap<String,String>();
		String onlyIndex = SnUtils.getOnlyIndex(companyId, projectId, orgItemId , preformId);
		map.put("onlyIndex", onlyIndex);
		if(dao == null){
			dao = getDao();
		}
		String code = "";
		List<SerialNum> snList = dao.selectByMap(map);
		//snlist数量大于1，表示出现异常情况
		
		//snlist为空，表示这是第一次发文件
		if(snList.size() == 0){
			if("|sn|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){//默认01,位数为三位.
				code = "001";
			}else if("yearsn".equals(snType)){//默认 2015 + 001,位数为三位.
				String year = SnUtils.getCurYear();
				code = year + "-" + "001";
			}else if("cmsn".equals(snType)){//默认001,前面两位,后面一位.
				code = "0"+ "-" + "0" + "-" + "1";  
			}
		}else{
			SerialNum sn = snList.get(0);
			//循环规则
			ISnRule snRule = null;
			if("|sn|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){//默认01,位数为三位.
				snRule = new SnRule(sn);
			}else if("yearsn".equals(snType)){
				snRule = new YearSnRule(sn);
			}else if("cmsn".equals(snType)){
				snRule = new CmSnRule(sn);
			}
			snRule.setNextValue();  //判断是否循环并设置下一个值
			
			int curValue = sn.getCurValue();
			if("sn".equals(snType) || "prefsn".equals(snType) || "brsn".equals(snType) || "brdatasn".equals(snType) || "brprojsn".equals(snType)){
				code = SnUtils.snComplement(curValue,3);
			}else if("yearsn".equals(snType)){
				String year = SnUtils.getCurYear();
				code = year + "-" + SnUtils.snComplement(curValue,3);
			}else if("cmsn".equals(snType)){
				String rltStr = SnUtils.snComplement(curValue,3);
				code = rltStr.substring(0,1) + "-" + rltStr.substring(1,2) + "-" + rltStr.substring(2, 3);
			}
		}
		jsonObj.put("sn", code);
		return jsonObj;
	}
	
	/*
	 *  获得保存用的流水号（不可以出现重复）
	 */
	@Override
	public SnEntity lockSaveSn(JSONObject reqJson) throws Exception {
		SnEntity snEnt = new SnEntity();
		String companyId = reqJson.optString("companyId");
		String projectId = reqJson.optString("projectId");
		String orgItemId = reqJson.optString("orgItemId");
		String preformId = reqJson.optString("preformId");
		String _id = reqJson.optString("_id");
		preformId = SnUtils.getPreformId(_id, companyId, preformId);
		
		TfPreformModel pf = SnUtils.getPreform(preformId);
		String snType = pf.getSnType();
		if(StringsUtils.isEmpty(snType) || "mulsn".equals(snType) || "secsn".equals(snType)){
			return snEnt;
		}
		
		if(StringsUtils.isEmpty(companyId)){
			Map<String, String> map = new HashMap<String,String>();
			map.put("value", "companyId");
			throw new FrameException("3000000000000052",map);
		}
		
		Map<String,String> map = new HashMap<String,String>();
		String onlyIndex = SnUtils.getOnlyIndex(companyId, projectId, orgItemId, preformId);
		map.put("onlyIndex", onlyIndex);
		
		if(dao == null){
			dao = getDao();
		}
		List<SerialNum> snList = dao.selectByMap(map);
				
		//snlist为空，表示这是第一次发文件
		if(snList.size() == 0){
			saveSerialNum( companyId, projectId, orgItemId, preformId, snType, 0);
			if("sn".equals(snType)){//默认01,位数为三位.
				snEnt.setSn("001");
				snEnt.setNo("001");
			}else if("yearsn".equals(snType)){//默认 2015 + 001,位数为三位.
				String year = SnUtils.getCurYear();
				snEnt.setSn(year + "-" + "001");
				snEnt.setNo("001");
			}else if("cmsn".equals(snType)){//默认001,前面两位,后面一位.
				snEnt.setSn("0"+ "-" + "0" + "-" + "1");
				snEnt.setNo("001");
			}else if("|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){
				snEnt.setSn(reqJson.optString("prefVal") + "001");
				snEnt.setNo("001");
			}
		}else{
			//查询两遍是为了防止锁住多条记录 (唯一索引一般情况下，不会出现多条记录)
			SerialNum sn = snList.get(0);
			String snId = sn.getId();
			//根据id把这条记录锁住,目的是防止流水号重复
			Map<String,String> paramMap = new HashMap<String, String>();
			paramMap.put("id", snId);
			SerialNum lockSn = dao.selectById4Lock(paramMap);//这里有并发情况，锁定此条记录
			
			//循环规则
			ISnRule snRule = null;
			if("|sn|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){
				snRule = new SnRule(lockSn);
			}else if("yearsn".equals(snType)){
				snRule = new YearSnRule(lockSn);
			}else if("cmsn".equals(snType)){
				snRule = new CmSnRule(lockSn);
			}
			snRule.setNextValue();  //判断是否循环并设置下一个值
			this.update(lockSn);//更新当前序列的值
			
			int curValue = lockSn.getCurValue();
			if("sn".equals(snType) ){
				snEnt.setSn(SnUtils.snComplement(curValue,3));
				snEnt.setNo(SnUtils.snComplement(curValue,3));
			}else if("yearsn".equals(snType)){
				String year = SnUtils.getCurYear();
				snEnt.setSn(year + "-" + SnUtils.snComplement(curValue,3));
				snEnt.setNo(SnUtils.snComplement(curValue,3));
			}else if("cmsn".equals(snType)){
				String rltStr = SnUtils.snComplement(curValue,3);
				snEnt.setSn(rltStr.substring(0,1) + "-" + rltStr.substring(1,2) + "-" + rltStr.substring(2, 3));
				snEnt.setNo(SnUtils.snComplement(curValue,3));
			}else if("|prefsn|brsn|brdatasn|brprojsn|".indexOf("|" + snType + "|") != -1){
				snEnt.setSn(reqJson.optString("prefVal") + SnUtils.snComplement(curValue,3));
				snEnt.setNo(SnUtils.snComplement(curValue,3));
			}
		}
		return snEnt;
	}
	
	@Override
	public int saveSnCurValue(JSONObject reqJson) throws Exception {
		String companyId = reqJson.optString("companyId");
		String projectId = reqJson.optString("projectId");
		String orgItemId = reqJson.optString("orgItemId");
		String preformId = reqJson.optString("preformId");
		String curValueStr = reqJson.optString("curValue");
		Map<String,String> map = new HashMap<String,String>();
		
		int curValue = 0;
		try{
			curValue = Integer.parseInt(curValueStr);
		}catch(Exception e){
			map.put("value", "curValue");
			throw new FrameException("3000000000000052",map);
		}
		
		if(curValue < 0 ){
			map.put("value", "curValue");
			throw new FrameException("3000000000000058",map);
		}
		
		if(StringsUtils.isEmpty(companyId)){
			map.put("value", "companyId");
			throw new FrameException("3000000000000052",map);
		}
		if(StringsUtils.isEmpty(preformId)){
			map.put("value", "preformId");
			throw new FrameException("3000000000000052",map);
		}
		
		TfPreformModel pf = SnUtils.getPreform(preformId);
		String snType = pf.getSnType();
		if(StringsUtils.isEmpty(snType)){
			return -1;
		}
		
		String onlyIndex = SnUtils.getOnlyIndex(companyId, projectId, orgItemId, preformId);
		map.put("onlyIndex", onlyIndex);
		
		if(dao == null){
			dao = getDao();
		}
		List<SerialNum> snList = dao.selectByMap(map);
		if(snList.size() == 0){
			saveSerialNum(companyId,projectId,orgItemId,preformId,snType,curValue);
		}else{
			updateSerialNum(snList.get(0),curValue);
		}
		return curValue;
	}

	@Override
	public boolean isFirstUsePreform(JSONObject reqJson) throws Exception {
		String companyId = reqJson.optString("companyId");
		String projectId = reqJson.optString("projectId");
		String orgItemId = reqJson.optString("orgItemId");
		String preformId = reqJson.optString("preformId");
		
		Map<String,String> map = new HashMap<String,String>();
		if(StringsUtils.isEmpty(companyId)){
			map.put("value", "companyId");
			throw new FrameException("3000000000000052",map);
		}
		if(StringsUtils.isEmpty(preformId)){
			map.put("value", "preformId");
			throw new FrameException("3000000000000052",map);
		}
		
		String onlyIndex = SnUtils.getOnlyIndex(companyId, projectId, orgItemId, preformId);
		map.put("onlyIndex", onlyIndex);
		if(dao == null){
			dao = getDao();
		}
		List<SerialNum> snList = dao.selectByMap(map);
		if(snList.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	public static void main(String[]args){
		String rltStr = SnUtils.snComplement(998,2);
		System.out.println(rltStr);
		System.out.println(rltStr.substring(0,1) + "-" + rltStr.substring(1, 2) + "-" + rltStr.substring(2, 3));
	}

	private void saveSerialNum(String companyId, String projectId,String orgItemId,
			String preformId,String snType,int curValue) throws Exception {
		IFrameSequence sF = FrameFactory.getSequenceFactory(null);
		SerialNum sn = new SerialNum();
		sn.setId(sF.getSequence());
		sn.setCompanyId(companyId);
		sn.setProjectId(projectId);
		sn.setPreformId(preformId);
		sn.setSnType(snType);
		sn.setCreateDate(new Date());
		sn.setCurValue(curValue);
		
		if("yearsn".equals(snType)){
			sn.setYear(new SimpleDateFormat("yyyy").format(new Date()));
		}
		String onlyIndexValue = SnUtils.getOnlyIndex(companyId, projectId, orgItemId, preformId);
		sn.setOnlyIndex(onlyIndexValue);//组合字段做唯一索引
		this.insert(sn);
	}
	

	private void updateSerialNum(SerialNum serialNum, int curValue) throws Exception {
		serialNum.setCurValue(curValue);
		this.update(serialNum);
	}
}
