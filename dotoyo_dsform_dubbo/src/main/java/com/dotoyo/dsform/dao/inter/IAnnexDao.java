package com.dotoyo.dsform.dao.inter;
import java.util.List;
import java.util.Map;

import com.dotoyo.dsform.model.AnnexModel;

public interface IAnnexDao extends IDao{
//annex{{{{
		/**
		 * 取分页记录
		 * @param conditon
		 * @return
		 * @throws Exception
		 **/
		 public List<Map<String,Object>> query(Map<String,String> condition)throws Exception;

		/**
		 * 取所有符合条件的记录数量
		 * @param conditon
		 * @return
		 * @throws Exception
		 **/
		 public long queryCount(Map<String,String> condition)throws Exception;

		 public List<AnnexModel> selectList(Map<String,String> condition) throws Exception;

//annex}}}}
	//annex[[[[
		public int insert(AnnexModel record)throws Exception;

		public int insert4Map(Map record)throws Exception;

		public int deleteByPrimaryKey(AnnexModel record)throws Exception;

		public int delete(AnnexModel record)throws Exception;

		public int updateByPrimaryKey(AnnexModel record)throws Exception;

		public int updateByPrimaryKey4Map(Map record)throws Exception;

		public AnnexModel selectByPrimaryKey(AnnexModel record)throws Exception;

		public List<Map> selectList4Map(Map record)throws Exception;

		public List<AnnexModel> selectModelList(AnnexModel record)throws Exception;

	//annex]]]]
	
	/**
	 * 获取无效的附件
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryInvalidAnnex(Map<String, Object> map);

	/**
	 * 获取要直接删除的附件
	 * @return
	 */
	public List<Map<String, Object>> queryDelAnnex();
	
}
