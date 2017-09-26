package com.dotoyo.dsform.test;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.dotoyo.dsform.interf.ITestService;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class TestService implements ITestService {
	
//	@Resource(name="buzzElementService")
//    private IBuzzElementService bes;
	
	@Autowired
    private MongoTemplate mongoTemplate;
	private final static String COLLECTION_NAME = "buzz_element"; 

	@Override
	public void testSaveMongo(String desc, String price, String itemName) {
//    	String itemName = "name";
//    	String price = "18";
//    	String desc = "商品价格";
        BuzzElement be = new BuzzElement();
        be.setItemName(itemName);
        be.setDesc(desc);
        be.setPrice(price);
        try {
            this.save(be);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
	}
	
    public int save(BuzzElement itemInfo) throws Exception {      
        DBCollection collection = mongoTemplate.getCollection(COLLECTION_NAME);
        int result = 0;
        DBObject iteminfoObj = BeanUtil.bean2DBObject(itemInfo);
        
        //iteminfoObj.removeField("serialVersionUID");
        //result = collection.insert(iteminfoObj).getN();
        WriteResult writeResult = collection.save(iteminfoObj);
        result = writeResult.getN();
        return result;
    }
	
 

}
