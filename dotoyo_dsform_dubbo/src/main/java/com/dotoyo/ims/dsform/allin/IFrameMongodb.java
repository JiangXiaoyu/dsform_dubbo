package com.dotoyo.ims.dsform.allin;
public interface IFrameMongodb {
}

//
//import java.net.UnknownHostException;
//import java.util.List;
//
//import org.springframework.data.document.mongodb.query.Query;
//import org.springframework.data.document.mongodb.query.Update;
//
////import org.springframework.data.document.mongodb.query.Query;
////import org.springframework.data.document.mongodb.query.Update;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//
///**
// * 数据库接口类
// * 
// * @author xieshh
// */
//public interface IFrameMongodb {
//	/**
//	 * 单条数据的插入
//	 * 
//	 * @param collection
//	 * @param o
//	 *            数据封装在DBObject中
//	 */
//	public void insert(String collection, DBObject o) throws Exception;
//
//	/**
//	 * 多条数据插入
//	 * 
//	 * @param collection
//	 * @param list
//	 */
//	public void insertList(String collection, List<DBObject> list)
//			throws Exception;
//
//	/**
//	 * 获得总记录数
//	 * 
//	 * @param collection
//	 * @return
//	 */
//
//	public long getCount(String collection) throws Exception;
//
//	/**
//	 * 删除表的所有数据
//	 * 
//	 * @param collection
//	 */
//	public void deletAll(String collection) throws Exception;
//
//	/**
//	 * 根据具体条件删除
//	 * 
//	 * @param collection
//	 * @param Json
//	 *            限制条件
//	 */
//	public void deleteByJson(String collection, String Json) throws Exception;
//
//	/**
//	 * 根据_id删除
//	 * 
//	 * @param collection
//	 * @param id
//	 */
//	public void deleteById(String collection, String id) throws Exception;
//
//	/**
//	 * 根据_id更新
//	 * 
//	 * @param collection
//	 * @param q
//	 *            需要更新的数据
//	 * @param _id
//	 *            id作为限制条件
//	 */
//	public int updateById(String collection, DBObject q, String _id)
//			throws Exception;
//
//	/**
//	 * 更新数据
//	 * 
//	 * @param collection
//	 * @param setCondMuitl
//	 *            需要更新的收据
//	 * @param objCondMuitl
//	 *            限制条件
//	 */
//	public int update(String collection, DBObject setCondMuitl,
//			DBObject objCondMuitl) throws Exception;
//
//	/**
//	 * 查询所有记录
//	 * 
//	 * @param collection
//	 * @return
//	 */
//	public DBCursor find(String collection) throws Exception;
//
//	/**
//	 * 根据条件查询一条记录
//	 * 
//	 * @param collection
//	 * @param json
//	 * @return
//	 */
//	public DBObject findOneByJson(String collection, String json)
//			throws Exception;
//
//	/**
//	 * 根据id查找
//	 * 
//	 * @param collection
//	 * @param _id
//	 * @return
//	 */
//	public DBObject findById(String collection, BasicDBObject obj)
//			throws Exception;
//
//	/**
//	 * 根据条件查询所有满足条件的记录
//	 * 
//	 * @param collection
//	 * @param json
//	 * @return
//	 */
//	public DBCursor findAllByJson(String collection, String json)
//			throws Exception;
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
//	 */
//	public DBCursor findAllByJsonSort(String collection, String json,
//			String col, String flag) throws Exception;
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
//	 */
//	public DBCursor findPagination(String collection, int startIndex,
//			int pageNum) throws Exception;
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
//	 */
//	public DBCursor findPagination(String collection, BasicDBObject condition,
//			int startIndex, int pageNum) throws Exception;
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
//	 */
//	public DBCursor findPaginationSort(String collection,
//			BasicDBObject condition, int startIndex, int pageNum,
//			BasicDBObject sort) throws Exception;
//
//	/**
//	 * 创建索引
//	 * 
//	 * @param collection
//	 * @param row
//	 *            建索引的列
//	 * @throws Exception
//	 */
//	public void createIndex(String collection, String row) throws Exception;
//
//	/**
//	 * 查询索引
//	 * 
//	 * @param collection
//	 * @return
//	 * @throws UnknownHostException
//	 */
//	public List<DBObject> getIndex(String collection) throws Exception;
//
//	public List find(String dbCode, String collection, Class<?> cls)
//			throws Exception;
//
//	public void insert(String dbCode, String collection, Object obj)
//			throws Exception;
//
//	public void insertList(String dbCode, String collection, List list)
//			throws Exception;
//
//	public long getAllCount(String dbCode, String collection) throws Exception;
//
//	/**
//	 * 删除表的所有数据
//	 * 
//	 * @param collection
//	 * @throws Exception
//	 */
//	public void deletAll(String dbCode, String collection) throws Exception;
//
//	public void deleteById(String dbCode, String collection, String id)
//			throws Exception;
//
//	/**
//	 * 删除数据
//	 * 
//	 * @param collection
//	 * @throws Exception
//	 */
//	public void delete(String dbCode, String collection, Query query)
//			throws Exception;
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
//			String id) throws Exception;
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
//			Query query) throws Exception;
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
//			Class<?> cls) throws Exception;
//
//	public Object findOne(String dbCode, String collection, String json,
//			Class<?> cls) throws Exception;
//
//	public Object findOne(String dbCode, String collection, Query query,
//			Class<?> cls) throws Exception;
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
//			throws Exception;
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
//			int startIndex, int pageNum, Class<?> cls) throws Exception;
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
//			int startIndex, int pageNum, Class<?> cls) throws Exception;
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
//			throws Exception;
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
//			Query query) throws Exception;
//}
