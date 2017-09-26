package com.dotoyo.dsform.interf;

import java.io.File;

import com.mongodb.DBObject;

public interface IFormTmplService {
	
	
	public File viewPreform2(String sign, String id, String _id, String type, String heightType, String type2,
			String creditSys, String orgId, String test) throws Exception;
	
	public DBObject findMongoDataById(String _id) throws Exception;

}
