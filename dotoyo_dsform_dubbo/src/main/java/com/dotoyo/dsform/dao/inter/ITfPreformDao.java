
package com.dotoyo.dsform.dao.inter;

import java.util.List;
import java.util.Map;

import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.ims.dsform.allin.PreformElm;

public interface ITfPreformDao {
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> query(Map<String,String> condition) throws Exception;

	public List<TfPreformModel> selectList(Map<String,String> condition) throws Exception;

	public List<TfPreformModel> selectListAll(Map<String,String> condition) throws Exception;
	
	public List<String> selectAllIds(Map<String,String> condition) throws Exception;
	
	public List<String> selectIds4Limit(Map<String,String> condition) throws Exception;
	
	/*
	 * 查询未编译的数量
	 */
	public long countUnrebuilde(Map<String, String> condition);
	
	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryCount(Map<String,String> condition) throws Exception;
	
	public int insert(TfPreformModel record)throws Exception;

	public int insert4Map(Map record)throws Exception;

	public int deleteByPrimaryKey(TfPreformModel record)throws Exception;

	public int updateByPrimaryKey(TfPreformModel record)throws Exception;

	public int updateByPrimaryKey4Map(Map record)throws Exception;

	public TfPreformModel selectByPrimaryKey(TfPreformModel record)throws Exception;
	
	public TfPreformModel selectByCode(TfPreformModel record)throws Exception;

	public List<Map> selectList4Map(Map record)throws Exception;

	public int insertPreformElm(PreformElm elm);


}
