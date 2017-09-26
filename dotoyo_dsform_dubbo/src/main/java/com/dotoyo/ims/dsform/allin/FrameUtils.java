
package com.dotoyo.ims.dsform.allin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dotoyo.dsform.model.TfPreformModel;
import com.dotoyo.dsform.util.StringsUtils;
import com.mongodb.DBObject;

import net.sf.json.JSONObject;

/**
 * 类工具
 * 
 * @author xieshh
 * 
 */
public class FrameUtils {
	private FrameUtils() {

	}

	final public static Object getService(String svCode,
			Map<String, String> param) throws Exception {
		if (param == null) {
			param = new HashMap<String, String>();
		}
		IFrameService svF = FrameFactory.getServiceFactory(param);
		return svF.getService(svCode);
	}

	final public static Object getService(Class<?> cls,
			Map<String, String> param) throws Exception {
		if (param == null) {
			param = new HashMap<String, String>();
		}
		IFrameService svF = FrameFactory.getServiceFactory(param);
		return svF.getService(cls);
	}

	final public static String getWords(String code, String msg,
			Map<String, String> param) throws Exception {
		IFrameLanguage lF = FrameFactory.getLanguageFactory(param);
		if (param == null) {
			return lF.getWords(code);
		} else {
			return lF.getWords(code, msg, param);
		}
	}

	/**
	 * 只适用于普通的没有嵌套的JSON对象
	 */
	public static Map<String, String> getMapFromJson(JSONObject reqJson)
			throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		for (Object key : reqJson.keySet()) {
			if (key instanceof String) {
				String tKey = (String) key;
				String value = reqJson.getString(tKey);
				param.put(tKey, value);
			}
		}
		return param;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Object getObjectFromJson(JSONObject reqJson,Class c)
			throws Exception {
		Set<String> keys = reqJson.keySet();
		Object instance = c.newInstance();
		Method[] methods = c.getDeclaredMethods();
		for(String key : keys){
			String value = reqJson.optString(key);
			if(!StringsUtils.isEmpty(value)){
				String setMethodName = "set" + key.substring(0,1).toUpperCase() + key.substring(1);
				for(Method method : methods){
					if(method.getName().equals(setMethodName)){
						Class[] cls = method.getParameterTypes();
						if(cls.length == 1 ){
							if(cls[0].getName().equals("java.lang.String")){
								method.invoke(instance, value);
							}
						}
						
					}
				}
			}
		}
		return instance;
	}
	
	public static void main(String[]args) throws Exception{
		JSONObject object = new JSONObject();
		object.put("name", "1");
		object.put("note", "2");
		
		TfPreformModel model = (TfPreformModel)FrameUtils.getObjectFromJson(object,TfPreformModel.class);
		System.out.println(model.getName());
		System.out.println(model.getNote());
	}

	/**
	 * 只适用于普通的没有嵌套的JSON对象
	 */
	public static Map<String, String> getMapFromDBObject(DBObject dbObj)
			throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		for (Object key : dbObj.keySet()) {
			if (key instanceof String) {
				String tKey = (String) key;
				String value = dbObj.get(tKey).toString();
				param.put(tKey, value);
			}
		}
		return param;
	}

	/**
	 * 当前虚拟机已使用内存
	 * @return
	 */
	public static String getUsedMemory() {
		Runtime r = Runtime.getRuntime();
		long first = r.totalMemory()-r.freeMemory();
		return (first ) + "";
	}
	
	/**
	 * 当前虚拟机已使用空余内存
	 * @return
	 */
	public static String getFreeMemory() {
		Runtime r = Runtime.getRuntime();
		long first = r.freeMemory();
		
		return (first ) + "";
	}
	
	/**
	 * 当前虚拟机已使用空余内存
	 * @return
	 */
	public static String getAllMemory() {
		Runtime r = Runtime.getRuntime();
		long first = r.totalMemory();
		
		return (first ) + "";
	}

}
