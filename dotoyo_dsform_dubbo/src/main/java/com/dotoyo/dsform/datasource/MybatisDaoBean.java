package com.dotoyo.dsform.datasource;

import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.dotoyo.dsform.util.StringsUtils;
import com.dotoyo.ims.dsform.allin.DatasourceKeyValuesBean;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.dsform.log.LogProxy;

public class MybatisDaoBean extends SqlSessionFactoryBean {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(MybatisDaoBean.class));
	public MybatisDaoBean(){
		log.debug("MybatisDaoBean");
	}
	@Override
	public void setConfigLocation(Resource configLocation) {
		if (configLocation != null) {
			//String location = configLocation.getURL().toString();

			//PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
			//Resource t=r.getResource(location);
			super.setConfigLocation(configLocation);
		}
	}

	@Override
	public void setDataSource(DataSource dataSource) {

		super.setDataSource(dataSource);
	}

	public void setMapperLocations(Resource[] mapperLocations) {
		if (mapperLocations != null && mapperLocations.length > 0) {
			String code = mapperLocations[0].getFilename();

			if (!StringsUtils.isEmpty(code)) {
				Map<String, String> map = DatasourceKeyValuesBean.getInstance()
						.getValue(code);
				if (map != null) {
					String dbType = map.get("dbType");
					if (!StringsUtils.isEmpty(dbType)) {
						String url = String.format(
								"classpath*:mybatis/%s/**/*.xml", dbType);
						PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
						try {
							Resource[] rs = r.getResources(url);
							super.setMapperLocations(rs);
						} catch (IOException e) {
							log.error("", e);
						}

					}
				}

			}
		}
	}

	/**
	 * @deprecated
	 * @param mapperLocations
	 */
	public void setMapperLocations1(Resource[] mapperLocations) {
		if (mapperLocations != null && mapperLocations.length > 0) {
			String code = mapperLocations[0].getFilename();

			if (!StringsUtils.isEmpty(code)) {
				Map<String, String> map = DatasourceKeyValuesBean.getInstance()
						.getValue(code);
				if (map != null) {
					String dbType = map.get("dbType");
					if (!StringsUtils.isEmpty(dbType)) {
						String url = String.format(
								"classpath*:mybatis/%s/**/*.xml", dbType);

						ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext(
								url);
						Resource[] rs = new Resource[0];
						try {
							rs = c.getResources(url);

						} catch (Throwable e) {
							log.error("", e);
						}
						super.setMapperLocations(rs);
					}
				}

			}
		}
	}

	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		SqlSessionFactory factory=super.buildSqlSessionFactory();
		return factory;
	}
}
