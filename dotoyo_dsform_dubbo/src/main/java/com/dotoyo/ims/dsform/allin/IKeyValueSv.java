
package com.dotoyo.ims.dsform.allin;

import java.util.List;
import java.util.Map;

/**
 * 所查询模块相关服务，
 * 
 * @author Administrator
 * 
 */
public interface IKeyValueSv {

	public String saveKeyValueData(Map<String,String>  map) throws Exception;

	public boolean deleteKeyValue(String ids);

	public  Map<String,Object> searchKeyValue(String id);

	public int updateKeyValueData(Map<String,String> map);

	public String saveKeyValueItemData(Map<String,String>  map);

	public void deleteKeyValueItem(String id);

	public Map<String,Object> searchKeyValueItem(String id);
	
	public List<Map<String,Object>> searchKeyValueItem(Map<String,String>  map);

	public int updateKeyValueItemData(Map<String,String>  map);

	public void deleteItemByKeyValueId(String id);
	
	public List<Map<String,String>> getKeyValue(Map<String,String>  map);

	public int enableKeyValue(String id);

	public int disableKeyValue(String id);
}
