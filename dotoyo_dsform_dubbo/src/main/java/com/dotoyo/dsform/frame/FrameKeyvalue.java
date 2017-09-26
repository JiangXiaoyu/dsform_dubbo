
package com.dotoyo.dsform.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.AbstractFrame;
import com.dotoyo.ims.dsform.allin.FrameException;
import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.IFrameKeyvalue;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.ims.dsform.allin.IFrameService;
import com.dotoyo.ims.dsform.allin.IKeyValueSv;

public class FrameKeyvalue extends AbstractFrame implements IFrameKeyvalue {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(FrameKeyvalue.class));

	protected static FrameKeyvalue instance = null;

	protected FrameKeyvalue() {

	}

	public static FrameKeyvalue getInstance(Map<String, String> map) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new FrameKeyvalue();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void startup() {
		// TODO Auto-generated method stub
		
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	public String getKeyValue(String module,String key) throws Exception {
		IFrameService svF = FrameFactory.getServiceFactory(null);
		Map<String,String> map = new HashMap<String,String>();
		map.put("module", module);
		map.put("key", key);
		IKeyValueSv sv = (IKeyValueSv) svF.getService(IKeyValueSv.class);
		List<Map<String,String>> result =  sv.getKeyValue(map);
		if(result==null){
			return "";
		}
		if(result.size() == 0){
			return "";
		}else if(result.size() > 1){
			throw new FrameException("3000000000000015", null);
		}
		Map<String,String> row=result.get(0);
		
		return row.get("TEXT");
	}

	public Map<String, String> getKeyValues(String module,String key)
			throws Exception {
		IFrameService svF = FrameFactory.getServiceFactory(null);
		Map<String,String> map = new HashMap<String,String>();
		map.put("module", module);
		map.put("key", key);
		IKeyValueSv sv = (IKeyValueSv) svF.getService(IKeyValueSv.class);
		List<Map<String,String>> result =  sv.getKeyValue(map);
		if(result.size() == 0){
			return null;
		}else if(result.size() > 1){
			throw new FrameException("3000000000000015", null);
		}
		return result.get(0);
	}
	
	public List<Map<String, String>> getKeyValueList(String module,String key)
			throws Exception {
		IFrameService svF = FrameFactory.getServiceFactory(null);
		Map<String,String> map = new HashMap<String,String>();
		map.put("module", module);   
		map.put("key", key);
		IKeyValueSv sv = (IKeyValueSv) svF.getService(IKeyValueSv.class);
		List<Map<String,String>> result =  sv.getKeyValue(map);
		return result;
	}
}
