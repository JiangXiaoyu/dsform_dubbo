package com.dotoyo.dsform.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.ims.dsform.allin.IFrameLog;

public class MybatisMapperBean extends MapperFactoryBean<Object> {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MybatisMapperBean.class));
	private static final Map<Class<Object>, MybatisMapperBean> map = new ConcurrentHashMap<Class<Object>, MybatisMapperBean>();
	private boolean isCenter = false;

	public MybatisMapperBean() {

		// log.debug("MybatisMapperBean");
	}

	public boolean getIsCenter() {
		return isCenter;
	}

	public void setIsCenter(boolean isCenter) {
		this.isCenter = isCenter;
	}

	@Override
	protected void checkDaoConfig() {

		super.checkDaoConfig();
	}

	@Override
	protected void initDao() throws Exception {

		super.initDao();
	}

	public void setMapperInterface(Class<Object> mapperInterface) {

		map.put(mapperInterface, this);
		super.setMapperInterface(mapperInterface);
	}

	@Override
	public void setAddToConfig(boolean addToConfig) {
		super.setAddToConfig(addToConfig);
	}

	@Override
	public Object getObject() throws Exception {
		Object obj = super.getObject();
		return obj;
	}

	@Override
	public Class<Object> getObjectType() {
		Class<Object> obj = super.getObjectType();

		return obj;
	}

	@Override
	public boolean isSingleton() {
		return super.isSingleton();
	}

	public static MybatisMapperBean getMapperBean(Class<?> obj) {
		return map.get(obj);
	}
	// public static MybatisMapperBean getMapperBean4Center(Class<?> obj) {
	// return map4Center.get(obj);
	// }
}
