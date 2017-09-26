
package com.dotoyo.dsform.dao.inter;

import java.util.List;
import java.util.Map;

public interface IKeyValueDao extends IDao{
	
	/**
	 * 取分页记录
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryKeyValueByPage(Map<String,String> condition) throws Exception;

	/**
	 * 取所有符合条件的记录数量
	 * @param conditon
	 * @return
	 * @throws Exception
	 */
	public long queryKeyValueRowCount(Map<String,String> condition) throws Exception;
	
	public List<Map<String,Object>> queryKeyValueItemByPage(Map<String,String> condition) throws Exception;
	
	public long queryKeyValueItemRowCount(Map<String,String> condition) throws Exception;
	
	public Object saveKeyValue(Map<String,String>  map);
	
	public int deleteKeyValue(String id);
	
	public Map<String,Object> searchKeyValue(String id);

	public int updateKeyValue(Map<String,String>  map);

	public Object saveKeyValueItem(Map<String,String>  map);

	public void deleteKeyValueItem(String id);

	public Map<String,Object> searchKeyValueItem(String id);
	
	public List<Map<String,Object>> searchKeyValueItem4Map(Map<String,String>  map);

	public int updateKeyValueItem(Map<String,String>  map);

	public void deleteItemByKeyValueId(String id);
	
	public List<Map<String,String>> getKeyValue(Map<String,String>  map);

	public int enableKeyValue(String id);

	public int disableKeyValue(String id);

}
