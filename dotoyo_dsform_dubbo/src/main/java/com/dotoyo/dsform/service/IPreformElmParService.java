
package com.dotoyo.dsform.service;

import java.util.List;
import java.util.Map;

public interface IPreformElmParService {
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> query(Map<String,String> condition) throws Exception;

	public List<PreformElmParModel> selectList(Map<String,String> condition) throws Exception;

	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryCount(Map<String,String> condition) throws Exception;
	
	public int insert(PreformElmParModel record)throws Exception;

	public int insert4Map(Map record)throws Exception;

	public int deleteByPrimaryKey(PreformElmParModel record)throws Exception;

	public int updateByPrimaryKey(PreformElmParModel record)throws Exception;

	public int updateByPrimaryKey4Map(Map record)throws Exception;

	public PreformElmParModel selectByPrimaryKey(PreformElmParModel record)throws Exception;

	public List<Map> selectList4Map(Map record)throws Exception;
}
