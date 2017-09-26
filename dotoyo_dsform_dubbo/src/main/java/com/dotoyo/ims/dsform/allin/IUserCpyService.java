package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

public interface IUserCpyService {
    
	public int insert(UserCpyModel record)throws Exception;

	public int insert4Map(Map record)throws Exception;

	public int deleteByPrimaryKey(UserCpyModel record)throws Exception;

	public int delete(UserCpyModel record)throws Exception;

	public int updateByPrimaryKey(UserCpyModel record)throws Exception;

	public int updateByPrimaryKey4Map(Map record)throws Exception;

	public UserCpyModel selectByPrimaryKey(UserCpyModel record)throws Exception;

	public List<Map> selectList4Map(Map record)throws Exception;

	public List<UserCpyModel> selectModelList(UserCpyModel record)throws Exception;
	
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> query(Map<String,String> condition) throws Exception;

	public List<UserCpyModel> selectList(Map<String,String> condition) throws Exception;

	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryCount(Map<String,String> condition) throws Exception;
}
