package com.dotoyo.ims.dsform.allin;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.model.IModel;

/**
 * 类工具
 * 
 * @author xieshh
 * 
 */
public class ClassUtils {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(ClassUtils.class));

	private ClassUtils() {

	}

	public static Object invokeStaticMethod(Method method, Class<?>[] cls,
			Object[] args) throws Exception {
		try {

			return method.invoke(cls, args);
		} catch (InvocationTargetException e) {

			throw new Exception(e.getTargetException());

		}
	}

	/**
	 * 把MAP换为实体
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> T map2Bean(Map<String, String> map, Class<T> class1)
			throws Exception {
		Field[] fields = class1.getDeclaredFields();
		T t = null;
		if (fields.length > 0) {
			t = class1.newInstance();
		}
		boolean flag;
		for (Field field : fields) {
			String key = field.getName();
			if (map.containsKey(key)) {
				Object value = map.get(key);
				if (value == null) {
					continue;
				}
				flag = false;
				if (!field.isAccessible()) {
					field.setAccessible(true);
					flag = true;
				}

				if ((field.getType() == java.util.Date.class || field.getType() == java.sql.Date.class)
						&& value.getClass() != field.getType()) {// 时间类型的转换
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					field.set(t, format.parse((String) value));
				} else if (field.getType() == java.sql.Timestamp.class
						&& value.getClass() != field.getType()) {// Timestamp转换
					field.set(t, Timestamp.valueOf((String) map.get(field
							.getName())));
				} else if (field.getType() == java.lang.Long.class
						&& value.getClass() != field.getType()) {// Long
					field.set(t, Long.valueOf((String) value));
				} else if ((field.getType() == int.class || field.getType() == java.lang.Integer.class)
						&& value.getClass() != field.getType()) {// int
					field.set(t, Integer.parseInt((String) value));
				} else {
					field.set(t, map.get((String) key));
				}
				if (flag) {
					field.setAccessible(false);
				}
			}
		}
		return t;
	}

	/**
	 * 把实体换为MAP
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> bean2Map(Object obj) throws Exception {

		if (obj == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();

		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();

			// 过滤class属性
			if (!key.equals("class")) {
				// 得到property对应的getter方法
				Method getter = property.getReadMethod();
				Object value = getter.invoke(obj);
				if (value == null) {
					map.put(key, null);

				} else if (value instanceof java.util.Date) {
					java.util.Date new_name = (java.util.Date) value;
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					map.put(key, format.format(new_name));

				} else if (value instanceof java.sql.Timestamp) {
					java.sql.Timestamp new_name = (java.sql.Timestamp) value;
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					map.put(key, format.format(new_name));
				} else {
					map.put(key, String.valueOf(value));
				}

			}

		}
		return map;

	}

	/**
	 * 
	 * 模型拷贝
	 * 
	 * @param obj
	 * @return
	 */
	public static void modelCopy(IModel model, Object obj) throws Exception {
		Class<?> class1 = obj.getClass();
		Field[] fields = class1.getDeclaredFields();
		boolean flag;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String name = field.getName();
			Object value = getFormModelByName(model, name);
			if (value == null) {
				continue;
			}
			flag = false;
			if (!field.isAccessible()) {
				field.setAccessible(true);
				flag = true;
			}

			if ((field.getType() == java.util.Date.class || field.getType() == java.sql.Date.class)
					&& value.getClass() != field.getType()) {// 时间类型的转换
				field.set(obj, Date.valueOf((String) value));
			} else if (field.getType() == java.sql.Timestamp.class
					&& value.getClass() != field.getType()) {// Timestamp转换
				field.set(obj, Timestamp.valueOf((String) value));
			} else if (field.getType() == java.lang.Long.class
					&& value.getClass() != field.getType()) {// Long
				field.set(obj, Long.valueOf((String) value));
			} else if ((field.getType() == int.class || field.getType() == java.lang.Integer.class)
					&& value.getClass() != field.getType()) {// int
				field.set(obj, Integer.parseInt((String) value));
			} else if ((field.getType() == boolean.class || field.getType() == java.lang.Boolean.class)
					&& value.getClass() != field.getType()) {// boolean
				field.set(obj, Boolean.parseBoolean((String) value));
			} else {
				field.set(obj, value);
			}
			if (flag) {
				field.setAccessible(false);
			}
		}

	}

	private static Object getFormModelByName(IModel model, String name)
			throws Exception {
		return null;
	}
}
