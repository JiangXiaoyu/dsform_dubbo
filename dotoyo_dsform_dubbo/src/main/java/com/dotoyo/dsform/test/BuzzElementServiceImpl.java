package com.dotoyo.dsform.test;

/**
 * @author "shihuc"
 * @date   2017年5月3日
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * @author chengsh05
 *
 */
@Service("buzzElementService")
public class BuzzElementServiceImpl implements IBuzzElementService {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    private final static String COLLECTION_NAME = "buzz_element"; 
    
    /* (non-Javadoc)
     * @see com.roomdis.center.mongo.service.IBuzzElementService#getItemInfo(org.json.JSONObject)
     */
    @Override
    public List<BuzzElement> getItemInfo(JSONObject json) throws Exception {
        List<BuzzElement> list = new ArrayList<BuzzElement>();
        // 判断查询的json中传递过来的参数
        DBObject query = new BasicDBObject();

        if(json.has("item_id")){            
            query.put("item_id", json.getString("item_id"));
        }else if(json.has("item_name")){
            query.put("item_name", json.getString("item_name"));
        }
        
        DBCursor results = mongoTemplate.getCollection(COLLECTION_NAME).find(query);
        if(null != results){
            Iterator<DBObject> iterator = results.iterator();
            while(iterator.hasNext()){
                BasicDBObject obj = (BasicDBObject) iterator.next();
                BuzzElement itemInfo = new BuzzElement();
                itemInfo = BeanUtil.dbObject2Bean(obj, itemInfo);
                list.add(itemInfo);
            }
        }
        
        
        return list;
    }

    /* (non-Javadoc)
     * @see com.roomdis.center.mongo.service.IBuzzElementService#save(com.roomdis.center.mongo.model.BuzzElement)
     */
    @Override
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

    /* (non-Javadoc)
     * @see com.roomdis.center.mongo.service.IBuzzElementService#update(com.roomdis.center.mongo.model.BuzzElement)
     */
    @Override
    public void update(BuzzElement intemInfo) throws Exception {
        DBCollection collection = this.mongoTemplate.getCollection(COLLECTION_NAME);
        BuzzElement queryItemInfo = new BuzzElement();
        queryItemInfo.setItemId(intemInfo.getItemId());
        DBObject itemInfoObj = BeanUtil.bean2DBObject(intemInfo);
        DBObject query =  BeanUtil.bean2DBObject(queryItemInfo);
        collection.update(query, itemInfoObj);        
    }
}
