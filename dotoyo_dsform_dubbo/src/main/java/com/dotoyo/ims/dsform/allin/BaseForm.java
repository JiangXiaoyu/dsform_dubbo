
package com.dotoyo.ims.dsform.allin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;

/**
 * 
 * 
 * @author xieshh
 * 
 */
public class BaseForm extends AbstractFrame implements IFrameForm {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(BaseForm.class));

	protected static BaseForm instance = null;

	protected BaseForm() {

	}

	public static BaseForm getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new BaseForm();
		}
	}

	public Map<String, Object> getFormData(String formCode,
			Map<String, Object> result) throws Exception {
			IFrameService svF = FrameFactory.getServiceFactory(null);
		try {
			IFormSv sv = (IFormSv) svF.getService(IFormSv.class);
			List<Map<String,Object>> re = sv.getFormElement(formCode);
			Map<String,String> map1 = new HashMap<String,String>();
			for(Map<String,Object> map : re){
				String col = map.get("COL").toString();
				String code = map.get("CODE").toString();
				map1.put(col, code);
			}
			
			Map<String,Object> newMap = new HashMap<String,Object>();
			for(Entry<String, Object> entry : result.entrySet()){
				String key = entry.getKey();
				Object value = entry.getValue();
				String newKey = map1.get(key);
				if(newKey != null){
					newMap.put(newKey, value);
				}else{
					newMap.put(key, value);
				}
			}
			return newMap;
		} catch (Exception e) {
			log.error("", e);
			
		}
		return null;
	}

	public void startup() {

	}

	public void shutdown() {

	}

}
