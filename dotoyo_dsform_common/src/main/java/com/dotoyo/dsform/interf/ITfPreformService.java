package com.dotoyo.dsform.interf;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.dotoyo.dsform.model.AnnexModel;
import com.dotoyo.dsform.model.TfPreformModel;

import net.sf.json.JSONObject;

public interface ITfPreformService {
	
//	public TfPreformModel selectByPrimaryKey(TfPreformModel model);
	
	public TfPreformModel selectByPrimaryKey(TfPreformModel record) throws Exception;
	
	public int insert(TfPreformModel record)throws Exception;
	
	public int deleteByPrimaryKey(TfPreformModel record)throws Exception;
	
	public String savePreform(AnnexModel bo, Map<String, String> map) throws Exception;
	
	public String savePreform4Multipage(AnnexModel bo, Map<String, String> map) throws Exception;
	
	public String getId(AnnexModel annexModel, Map<String, String> formValueMap);
	
	public String getSeqId();
	
	public void uploadFrameAnnex(AnnexModel annexModel, InputStream is, Map<String, String> formValueMap);
	
	public AnnexModel uploadFrameAnnex(AnnexModel annexModel, byte[] is, Map<String, String> formValueMap);
	
	public List<String> selectIds4Limit(Map<String, String> map) throws Exception;
	
	public long countUnrebuilde(Map<String, String> condition);
	
	public int updateByPrimaryKey(TfPreformModel record)throws Exception;

	public JSONObject getPreformData4Mongodb(JSONObject reqJson) throws Exception;
	
	/*
	 * 从Mongodb中存入 取出预定义报表数据
	 */
	public JSONObject savePreformData4Mongodb(JSONObject reqJson, JSONObject jsonObject)throws Exception ;
	
	public JSONObject loadPrintDirect(String id, String _id) throws Exception;
	
}
