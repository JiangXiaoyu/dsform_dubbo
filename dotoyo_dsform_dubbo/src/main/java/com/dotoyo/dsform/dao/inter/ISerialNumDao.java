
package com.dotoyo.dsform.dao.inter;

import java.util.List;
import java.util.Map;

import com.dotoyo.dsform.model.SerialNum;

public interface ISerialNumDao {
	
	public void insert(SerialNum sn) throws Exception;
	
	public void update(SerialNum sn) throws Exception;
	
	public SerialNum selectById4Lock(Map<String, String> map) throws Exception;
	
	public SerialNum selectById(Map<String, String> map) throws Exception;

	public List<SerialNum> selectByMap(Map<String, String> map);
}
