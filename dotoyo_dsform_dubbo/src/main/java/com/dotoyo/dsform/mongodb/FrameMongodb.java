package com.dotoyo.dsform.mongodb;

public class FrameMongodb {
	
}

//
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.logging.LogFactory;
//import org.bson.types.ObjectId;
////import org.springframework.data.document.mongodb.query.Criteria;
////import org.springframework.data.document.mongodb.query.Query;
////import org.springframework.data.document.mongodb.query.Update;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//
//import com.dotoyo.dsform.log.LogProxy;
//import com.dotoyo.dsform.util.StringsUtils;
//import com.dotoyo.ims.dsform.allin.AbstractFrame;
//import com.dotoyo.ims.dsform.allin.Base64Util;
//import com.dotoyo.ims.dsform.allin.FrameFactory;
//import com.dotoyo.ims.dsform.allin.IFrameCenter;
//import com.dotoyo.ims.dsform.allin.IFrameLog;
//import com.dotoyo.ims.dsform.allin.IFrameMongodb;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.mongodb.Mongo;
//import com.mongodb.MongoOptions;
//import com.mongodb.ServerAddress;
//import com.mongodb.WriteResult;
//import com.mongodb.util.JSON;
//
//public class FrameMongodb extends AbstractFrame implements IFrameMongodb {
//	protected static final transient IFrameLog log = new LogProxy(
//			LogFactory.getLog(FrameMongodb.class));
//
//	protected static FrameMongodb instance = null;
//
//	protected FrameMongodb() {
//
//	}
//
//	public static FrameMongodb getInstance(Map<String, String> param) {
//		if (instance == null) {
//			newInstance(param);
//		}
//		return instance;
//	}
//
//	synchronized private static void newInstance(Map<String, String> param) {
//		if (instance == null) {
//			instance = new FrameMongodb();
//		}
//	}
//
//	@Override
//	public void shutdown() {
//		try {
//			for (Mongo m : mongoMap.values()) {
//				try {
//					m.close();
//				} catch (Throwable e) {
//
//				}
//			}
//		} catch (Throwable e) {
//
//		}
//	}
//
//	@Override
//	public void startup() {
//
//	}
////	org.springframework.data.document.mongodb.
//	private MongoTemplate getMongoTemplate(
//			String dbCode) throws Exception {
//
//		Map<String, MongoTemplate> tempMap = getMongoTemplateMap();
//		String code = getDbCode(dbCode, tempMap);
//		return tempMap.get(code);
//	}
//
//	private Map<String, MongoTemplate> tempMap = null;
//	private Map<String, Mongo> mongoMap = null;
//
//	protected Map<String, MongoTemplate> getMongoTemplateMap()
//			throws Exception {
//		if (this.tempMap == null || this.tempMap.size() == 0) {
//			initailDbMap();
//		}
//		return this.tempMap;
//	}
//
//	synchronized protected void initailDbMap() throws Exception {
//		if (this.tempMap == null || this.tempMap.size() == 0) {
//			this.tempMap = new HashMap<String, MongoTemplate>();
//			this.mongoMap = new HashMap<String, Mongo>();
//
//			Map<String, Map<String, String>> keyValues = MongodbKeyValuesConfig
//					.getInstance().getKeyValues();
//			for (String key : keyValues.keySet()) {
//				Map<String, String> map = keyValues.get(key);
//				MongoTemplate t = null;
//				try {
//					t = initail(key, map);
//					if (t != null) {
//
//						tempMap.put(key, t);
//					}
//
//				} catch (Throwable e) {
//
//					if (t != null) {
//						try {
//							Mongo instance = this.mongoMap.get(key);
//							if (instance != null) {
//								try {
//									instance.close();
//								} catch (Throwable e1) {
//
//								}
//							}
//						} catch (Throwable e1) {
//
//						}
//					}
//					log.error("", e);
//				}
//
//			}
//		}
//	}
//
//	private MongoTemplate initail(
//			String key, Map<String, String> map) throws Exception {
//		String addsStr = map.get("adds");
//		if (StringsUtils.isEmpty(addsStr)) {
//			return null;
//		}
//		List<String> list = StringsUtils.split(addsStr, ',');
//		List<ServerAddress> adds = new ArrayList<ServerAddress>(list.size());
//		for (int i = 0; i < list.size(); i++) {
//			String add = list.get(i);
//			ServerAddress address = new ServerAddress(add);
//			adds.add(address);
//		}
//		MongoOptions options = new MongoOptions();
//		String connectionsPerHost = map.get("connectionsPerHost");
//		if (!StringsUtils.isEmpty(connectionsPerHost)
//				&& StringsUtils.isNumber(connectionsPerHost)) {
//			options.connectionsPerHost = Integer.parseInt(connectionsPerHost);
//		}
//		String threadsAllowedToBlockForConnectionMultiplier = map
//				.get("threadsAllowedToBlockForConnectionMultiplier");
//		if (!StringsUtils.isEmpty(threadsAllowedToBlockForConnectionMultiplier)
//				&& StringsUtils
//						.isNumber(threadsAllowedToBlockForConnectionMultiplier)) {
//			options.threadsAllowedToBlockForConnectionMultiplier = Integer
//					.parseInt(threadsAllowedToBlockForConnectionMultiplier);
//		}
//		String connectTimeout = map.get("connectTimeout");
//		if (!StringsUtils.isEmpty(connectTimeout)
//				&& StringsUtils.isNumber(connectTimeout)) {
//			options.connectTimeout = Integer.parseInt(connectTimeout);
//		}
//		String maxWaitTime = map.get("maxWaitTime");
//		if (!StringsUtils.isEmpty(maxWaitTime)
//				&& StringsUtils.isNumber(maxWaitTime)) {
//			options.maxWaitTime = Integer.parseInt(maxWaitTime);
//		}
//		String socketTimeout = map.get("socketTimeout");
//		if (!StringsUtils.isEmpty(socketTimeout)
//				&& StringsUtils.isNumber(socketTimeout)) {
//			options.socketTimeout = Integer.parseInt(socketTimeout);
//		}
//		String autoConnectRetry = map.get("autoConnectRetry");
//		if ("true".equals(autoConnectRetry)) {
//			options.autoConnectRetry = true;
//		} else {
//			options.autoConnectRetry = false;
//		}
//		String socketKeepAlive = map.get("socketKeepAlive");
//		if ("true".equals(socketKeepAlive)) {
//			options.socketKeepAlive = true;
//		} else {
//			options.socketKeepAlive = false;
//		}
//		//
//		Mongo instance = null;
//		try {
//			instance = new Mongo(adds, options);
//
//			//
//
//			String dbName = map.get("dbName");
//			if (StringsUtils.isEmpty(dbName)) {
//				return null;
//			}
//			// DB db = instance.getDB(dbName);
//
//			String user = map.get("user");
//			if (StringsUtils.isEmpty(user)) {
//				return null;
//			}
//
//			String passwd = map.get("passwd");
//			if (StringsUtils.isEmpty(passwd)) {
//				return null;
//			}
//			String encrypt = map.get("encrypt");
//			if ("true".equals(encrypt)) {
//
//				passwd = new String(Base64Util.decryptBASE64(passwd));
//
//			}
//			// db.authenticate(user, passwd.toCharArray());
//			MongoTemplate t = new MongoTemplate(
//					instance, dbName);
//			t.setUsername(user);
//			t.setPassword(passwd);
//			t.setDatabaseName(dbName);
//			this.mongoMap.put(key, instance);
//			return t;
//		} catch (Exception e) {
//			if (instance != null) {
//				try {
//					instance.close();
//				} catch (Throwable e1) {
//
//				}
//			}
//			throw e;
//		}
//	}
//
//	/**
//	 * 取得分中心后的数据库编码
//	 * 
//	 * @param code
//	 * @return
//	 * @throws Exception
//	 */
//	private String getDbCode(
//			String code,
//			Map<String, MongoTemplate> tempMap)
//			throws Exception {
//		if (StringsUtils.isEmpty(code)) {
//			return "default";
//		}
//		String sub = "";
//		if (!tempMap.containsKey(code)) {
//
//			IFrameCenter cen = (IFrameCenter) FrameFactory
//					.getCenterFactory(null);
//
//			sub = cen.getCurrentCenter();
//
//			if (StringsUtils.isEmpty(sub)) {
//				throw new Exception("no center define");
//			} else {
//				return String.format("%s_%s", code, sub);
//			}
//		} else {
//			return code;
//		}
//	}
//
//	public static void main(String[] args) throws Exception {
//		Person p = new Person();
//		p.setId("4");
//		FrameMongodb.getInstance(null).insert("default", "person", p);
////		List list = FrameMongodb.getInstance(null).find("default", "person",
////				Person.class);
////		List list1 = FrameMongodb.getInstance(null).findPagination("default",
////				"person", 0, 2, Person.class);
////		log.debug(list1);
////		log.debug(list);
//	}
//
//	public List find(String dbCode, String collection, Class<?> cls)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		return temp.find(collection, new Query(), Person.class);
//
//	}
//
//	/**
//	 * 单条数据的插入
//	 * 
//	 * @param collection
//	 * @param o
//	 *            数据封装在DBObject中
//	 * @throws Exception
//	 */
//	public void insert(String dbCode, String collection, Object obj)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		temp.insert(collection, obj);
//
//	}
//
//	/**
//	 * 多条数据插入
//	 * 
//	 * @param collection
//	 * @param list
//	 * @throws Exception
//	 */
//	public void insertList(String dbCode, String collection, List list)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		temp.insertList(collection, list);
//
//	}
//
//	/**
//	 * 获得总记录数
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 */
//
//	public long getAllCount(String dbCode, String collection) throws Exception {
//		long count = 0;
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		DBCollection coll = temp.getCollection(collection);
//		if (null != coll) {
//			count = coll.count();
//		}
//		return count;
//
//	}
//
//	/**
//	 * 删除表的所有数据
//	 * 
//	 * @param collection
//	 * @throws Exception
//	 */
//	public void deletAll(String dbCode, String collection) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		DBCollection coll = temp.getCollection(collection);
//		if (null != coll) {
//			coll.drop();
//		}
//
//	}
//
//	/**
//	 * 删除数据
//	 * 
//	 * @param collection
//	 * @throws Exception
//	 */
//	public void delete(String dbCode, String collection, Query query)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		temp.remove(query);
//	}
//
//	/**
//	 * 根据_id删除
//	 * 
//	 * @param collection
//	 * @param id
//	 * @throws Exception
//	 */
//	public void deleteById(String dbCode, String collection, String id)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		DBCollection coll = temp.getCollection(collection);
//
//		if (null != coll) {
//			ObjectId _id = new ObjectId(id);
//			BasicDBObject obj = new BasicDBObject();
//			obj.put("_id", _id);
//			coll.remove(obj);
//		}
//
//	}
//
//	/**
//	 * 根据_id更新
//	 * 
//	 * @param collection
//	 * @param q
//	 *            需要更新的数据
//	 * @param _id
//	 *            id作为限制条件
//	 * @throws Exception
//	 */
//	public int updateById(String dbCode, String collection, Object objBean,
//			String id) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		DBCollection coll = temp.getCollection(collection);
//		if (null != coll) {
//			ObjectId objId = new ObjectId(id);
//			BasicDBObject obj = new BasicDBObject();
//			DBObject set = new BasicDBObject();
//			set.put("$set", objBean);
//			obj.put("_id", objId);
//			WriteResult result = coll.update(obj, set);
//			if (result != null) {
//				String err = result.getError();
//				if (!StringsUtils.isEmpty(err)) {
//					throw new Exception(err);
//				}
//				return result.getN();
//			}
//		}
//		return 0;
//	}
//
//	/**
//	 * 更新
//	 * 
//	 * @param collection
//	 * @param q
//	 *            需要更新的数据
//	 * @param _id
//	 *            id作为限制条件
//	 * @throws Exception
//	 */
//	public int updateFirst(String dbCode, String collection, Update update,
//			Query query) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		WriteResult result = temp.updateFirst(query, update);
//		if (result != null) {
//			String err = result.getError();
//			if (!StringsUtils.isEmpty(err)) {
//				throw new Exception(err);
//			}
//			return result.getN();
//		}
//		return 0;
//	}
//
//	/**
//	 * 更新
//	 * 
//	 * @param collection
//	 * @param q
//	 *            需要更新的数据
//	 * @param _id
//	 *            id作为限制条件
//	 * @throws Exception
//	 */
//	public int updateMulti(String dbCode, String collection, Update update,
//			Query query) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		WriteResult result = temp.updateMulti(collection, query, update);
//		if (result != null) {
//			String err = result.getError();
//			if (!StringsUtils.isEmpty(err)) {
//				throw new Exception(err);
//			}
//			return result.getN();
//		}
//		return 0;
//	}
//
//	/**
//	 * 根据id查找
//	 * 
//	 * @param collection
//	 * @param _id
//	 * @return
//	 * @throws Exception
//	 */
//	public Object findById(String dbCode, String collection, String id,
//			Class<?> cls) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		Criteria criteria = Criteria.whereId().is(id);
//		if (criteria != null) {
//			Query query = new Query(criteria);
//			if (query != null) {
//				return temp.findOne(collection, query, cls);
//			}
//		}
//		return null;
//	}
//
//	public Object findOne(String dbCode, String collection, String json,
//			Class<?> cls) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//
//		DBObject objq = (DBObject) JSON.parse(json);
//		Query query = new Query();
//		for (String key : objq.keySet()) {
//			query.addCriteria(Criteria.where(key).is(objq.get(key)));
//		}
//
//		return temp.findOne(collection, query, cls);
//
//	}
//
//	public Object findOne(String dbCode, String collection, Query query,
//			Class<?> cls) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//
//		return temp.findOne(collection, query, cls);
//
//	}
//
//	/**
//	 * 根据具体条件删除
//	 * 
//	 * @param collection
//	 * @param Json
//	 *            限制条件
//	 * @throws Exception
//	 */
//	public void deleteByJson(String dbCode, String collection, String Json)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		DBCollection coll = temp.getCollection(collection);
//		if (null != coll) {
//			DBObject o = (DBObject) JSON.parse(Json);
//			coll.remove(o);
//		}
//
//	}
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public List findPagination(String dbCode, String collection,
//			int startIndex, int pageNum, Class<?> cls) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		Query query = new Query();
//		query.skip(startIndex);// skip相当于从那条记录开始
//		query.limit(pageNum);// 从skip开始,取多少条记录
//		List datas = temp.find(collection, query, cls);
//		return datas;
//
//	}
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public List findPagination(String dbCode, String collection, String json,
//			int startIndex, int pageNum, Class<?> cls) throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		Query query = new Query();
//		DBObject objq = (DBObject) JSON.parse(json);
//		for (String key : objq.keySet()) {
//			query.addCriteria(Criteria.where(key).is(objq.get(key)));
//		}
//		query.skip(startIndex);// skip相当于从那条记录开始
//		query.limit(pageNum);// 从skip开始,取多少条记录
//		List datas = temp.find(collection, query, cls);
//		return datas;
//
//	}
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public List find(String dbCode, String collection, Query query, Class<?> cls)
//			throws Exception {
//
//		MongoTemplate temp = getMongoTemplate(dbCode);
//		List datas = temp.find(collection, query, cls);
//		return datas;
//	}
//
//	/**
//	 * 单条数据的插入
//	 * 
//	 * @param collection
//	 * @param o
//	 *            数据封装在DBObject中
//	 * @throws Exception
//	 */
//	public void insert(String collection, DBObject o) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			coll.insert(o);
//		}
//
//	}
//
//	/**
//	 * 多条数据插入
//	 * 
//	 * @param collection
//	 * @param list
//	 * @throws Exception
//	 */
//	public void insertList(String collection, List<DBObject> list)
//			throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			coll.insert(list);
//		}
//
//	}
//
//	/**
//	 * 获得总记录数
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 */
//
//	public long getCount(String collection) throws Exception {
//		long count = 0;
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			count = coll.count();
//		}
//
//		return count;
//	}
//
//	/**
//	 * 获得Collection
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 */
//	protected DBCollection getConnection(String collection) throws Exception {
//		DBCollection coll = null;
//
//		MongoTemplate temp = getMongoTemplate("default");
//		DB db = temp.getDb();
//		db.getStats();
//		coll = db.getCollection(collection);
//
//		return coll;
//	}
//
//	/**
//	 * 删除表的所有数据
//	 * 
//	 * @param collection
//	 * @throws Exception
//	 */
//	public void deletAll(String collection) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			coll.drop();
//		}
//
//	}
//
//	/**
//	 * 根据具体条件删除
//	 * 
//	 * @param collection
//	 * @param Json
//	 *            限制条件
//	 * @throws Exception
//	 */
//	public void deleteByJson(String collection, String Json) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject o = (DBObject) JSON.parse(Json);
//			coll.remove(o);
//		}
//
//	}
//
//	/**
//	 * 根据_id删除
//	 * 
//	 * @param collection
//	 * @param id
//	 * @throws Exception
//	 */
//	public void deleteById(String collection, String id) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			ObjectId _id = new ObjectId(id);
//			BasicDBObject obj = new BasicDBObject();
//			obj.put("_id", _id);
//			coll.remove(obj);
//		}
//
//	}
//
//	/**
//	 * 根据_id更新
//	 * 
//	 * @param collection
//	 * @param q
//	 *            需要更新的数据
//	 * @param _id
//	 *            id作为限制条件
//	 * @throws Exception
//	 */
//	public int updateById(String collection, DBObject q, String _id)
//			throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			ObjectId id = new ObjectId(_id);
//			BasicDBObject obj = new BasicDBObject();
//			DBObject set = new BasicDBObject();
//			set.put("$set", q);
//			obj.put("_id", id);
//			WriteResult result = coll.update(obj, set);
//			if (result != null) {
//				String err = result.getError();
//				if (!StringsUtils.isEmpty(err)) {
//					throw new Exception(err);
//				}
//				return result.getN();
//			}
//		}
//		return 0;
//	}
//
//	/**
//	 * 更新数据
//	 * 
//	 * @param collection
//	 * @param setCondMuitl
//	 *            需要更新的收据
//	 * @param objCondMuitl
//	 *            限制条件
//	 * @throws Exception
//	 */
//	public int update(String collection, DBObject setCondMuitl,
//			DBObject objCondMuitl) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject set = new BasicDBObject();
//			set.put("$set", setCondMuitl);
//			com.mongodb.WriteResult result = coll.update(objCondMuitl, set,
//					false, true);
//			if (result != null) {
//				String err = result.getError();
//				if (!StringsUtils.isEmpty(err)) {
//					throw new Exception(err);
//				}
//				return result.getN();
//			}
//		}
//		return 0;
//	}
//
//	/**
//	 * 查询所有记录
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor find(String collection) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			return coll.find();
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 根据条件查询一条记录
//	 * 
//	 * @param collection
//	 * @param json
//	 * @return
//	 * @throws Exception
//	 */
//	public DBObject findOneByJson(String collection, String json)
//			throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject objq = (DBObject) JSON.parse(json);
//			return coll.findOne(objq);
//		} else {
//
//			return null;
//		}
//
//	}
//
//	/**
//	 * 根据id查找
//	 * 
//	 * @param collection
//	 * @param _id
//	 * @return
//	 * @throws Exception
//	 */
//	public DBObject findById(String collection, BasicDBObject obj)
//			throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject o = coll.findOne(obj);
//			return o;
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 根据条件查询所有满足条件的记录
//	 * 
//	 * @param collection
//	 * @param json
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor findAllByJson(String collection, String json)
//			throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject objq = (DBObject) JSON.parse(json);
//			return coll.find(objq);
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 排序查找
//	 * 
//	 * @param collection
//	 * @param json
//	 *            查找条件 json格式的字符串
//	 * @param col
//	 *            排序的列
//	 * @param flag
//	 *            升序 asc 降序 desc
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor findAllByJsonSort(String collection, String json,
//			String col, String flag) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			DBObject objq = (DBObject) JSON.parse(json);
//			String sortJson = "";
//			if (null != flag) {
//				if (flag.equals("asc")) {
//					sortJson = "{'" + col + "':1}";
//				} else if (flag.equals("desc")) {
//					sortJson = "{'" + col + "':-1}";
//				}
//			}
//			DBObject objSort = (DBObject) JSON.parse(sortJson);
//			return coll.find(objq).sort(objSort);
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor findPagination(String collection, int startIndex,
//			int pageNum) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			return coll.find().skip(startIndex).limit(pageNum);
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 分页查询
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor findPagination(String collection, BasicDBObject condition,
//			int startIndex, int pageNum) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//
//		if (null != coll) {
//			return coll.find(condition).skip(startIndex).limit(pageNum);
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 分页查询排序
//	 * 
//	 * @param collection
//	 * @param pageNum
//	 *            页大小
//	 * @param startIndex
//	 *            起始记录
//	 * @return
//	 * @throws Exception
//	 */
//	public DBCursor findPaginationSort(String collection,
//			BasicDBObject condition, int startIndex, int pageNum,
//			BasicDBObject sort) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		if (null != coll) {
//			return coll.find(condition).skip(startIndex).limit(pageNum).sort(
//					sort);
//		} else {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 创建索引
//	 * 
//	 * @param collection
//	 * @param row
//	 *            建索引的列
//	 * @throws Exception
//	 */
//	public void createIndex(String collection, String row) throws Exception {
//
//		DBCollection coll = getConnection(collection);
//		DBObject o = new BasicDBObject();
//		o.put(row, 1);
//		coll.createIndex(o);
//	}
//
//	/**
//	 * 查询索引
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws UnknownHostException
//	 */
//	public List<DBObject> getIndex(String collection) throws Exception {
//		DBCollection coll = getConnection(collection);
//		if (coll != null) {
//			List<DBObject> list = coll.getIndexInfo();
//			return list;
//		}
//		return new ArrayList<DBObject>();
//	}
//
//}
