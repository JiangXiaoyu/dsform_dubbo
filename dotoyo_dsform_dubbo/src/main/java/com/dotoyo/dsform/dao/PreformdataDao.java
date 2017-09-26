package com.dotoyo.dsform.dao;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.dotoyo.dsform.dao.inter.IPreformdataDao;
import com.dotoyo.dsform.test.BeanUtil;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class PreformdataDao implements IPreformdataDao {
	
	private MongoTemplate mongoTemplate;
	
	private final static String Collection_Name = "preformData";
	
	 @Override
	    public int save(Object itemInfo) throws Exception {      
	        DBCollection collection = mongoTemplate.getCollection(Collection_Name);
	        int result = 0;
	        DBObject iteminfoObj = BeanUtil.bean2DBObject(itemInfo);
	        
	        //iteminfoObj.removeField("serialVersionUID");
	        //result = collection.insert(iteminfoObj).getN();
	        WriteResult writeResult = collection.save(iteminfoObj);
	        result = writeResult.getN();
	        return result;
	    }

}
