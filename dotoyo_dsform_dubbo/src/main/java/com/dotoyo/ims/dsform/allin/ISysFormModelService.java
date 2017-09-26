
package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

public interface ISysFormModelService {
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> query(Map<String,String> condition) throws Exception;
	public List<SysFormModelModel> selectList(Map<String,String> condition) throws Exception;
	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryCount(Map<String,String> condition) throws Exception;
	public int insert(SysFormModelModel record)throws Exception;

	public int insert4Map(Map record)throws Exception;

	public int deleteByPrimaryKey(SysFormModelModel record)throws Exception;

	public int updateByPrimaryKey(SysFormModelModel record)throws Exception;

	public int updateByPrimaryKey4Map(Map record)throws Exception;

	public SysFormModelModel selectByPrimaryKey(SysFormModelModel record)throws Exception;

	public List<Map> selectList4Map(Map record)throws Exception;
	
	public List<Map> findFormsByDirId(Map map)throws Exception;
}
