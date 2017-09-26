
package com.dotoyo.dsform.frame;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.dao.inter.IKeyValueDao;
import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.AbstractService;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IKeyValueSv;

/**
 * 版本1.0
 * 
 * @author Administrator
 * 
 */
public class KeyValueSv extends AbstractService implements IKeyValueSv {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(KeyValueSv.class));
	private IKeyValueDao dao;

	public IKeyValueDao getDao() {
		return dao;
	}

	public void setDao(IKeyValueDao dao) {
		this.dao = dao;
	}

	public String saveKeyValueData(Map<String,String>  map) throws Exception {
		Object id = dao.saveKeyValue(map);
		return id.toString();
	}

	public boolean deleteKeyValue(String id) {
		int i = dao.deleteKeyValue(id);
		return i==0?false:true;
	}
	
	public void deleteItemByKeyValueId(String id) {
		dao.deleteItemByKeyValueId(id);
	}

	public void deleteKeyValueItem(String id) {
		 dao.deleteKeyValueItem(id);
	}

	public  Map<String,Object> searchKeyValue(String id) {
		return dao.searchKeyValue(id);
	}
	
	public Map<String,Object> searchKeyValueItem(String id) {
		return dao.searchKeyValueItem(id);
	}

	public List<Map<String,Object>> searchKeyValueItem(Map<String, String> map) {
		return dao.searchKeyValueItem4Map(map);
	}

	public int updateKeyValueData(Map<String,String>  map) {
		return dao.updateKeyValue(map);
	}

	public String saveKeyValueItemData(Map<String,String>  map) {
		Object id = dao.saveKeyValueItem(map);
		return id.toString();
	}

	public int updateKeyValueItemData(Map<String,String>  map) {
		return dao.updateKeyValueItem(map);
	}

	public List<Map<String, String>> getKeyValue(Map<String,String>  map) {
		return dao.getKeyValue(map);
	}

	public int enableKeyValue(String id) {
		return dao.enableKeyValue(id);
	}

	public int disableKeyValue(String id) {
		return dao.disableKeyValue(id);
	}
}
