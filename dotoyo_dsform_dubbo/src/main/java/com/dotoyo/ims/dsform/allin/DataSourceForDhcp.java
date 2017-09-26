package com.dotoyo.ims.dsform.allin;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.LogFactory;

import com.dotoyo.dsform.log.LogProxy;
import com.dotoyo.dsform.util.StringsUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceForDhcp extends AbstractFrame implements
		IFrameDatasource {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(DataSourceForDhcp.class));

	protected static DataSourceForDhcp instance = null;
	Map<String, FrameSqlDataSource> dsMap = new Hashtable<String, FrameSqlDataSource>();

	protected DataSourceForDhcp() {
		Map<String, Map<String, String>> keyValues = DatasourceKeyValuesBean
				.getInstance().getKeyValues();
		for (String key : keyValues.keySet()) {
			Map<String, String> map = keyValues.get(key);
			try {
				BasicDataSource ds = initail(map);
				if (ds != null) {
					FrameSqlDataSource d = new FrameSqlDataSource(ds);
					dsMap.put(key, d);
				}

			} catch (Throwable e) {
				log.error("", e);
			}

		}
	}

	private BasicDataSource initail(Map<String, String> map) throws Exception {
		String jdbcUrl = map.get("jdbcUrl");
		if (StringsUtils.isEmpty(jdbcUrl)) {
			return null;
		}
		String user = map.get("user");
		String encrypt = map.get("encrypt");
		String passwd = map.get("passwd");
		if ("true".equals(encrypt)) {
			if (!StringsUtils.isEmpty(passwd)) {
				passwd = new String(Base64Util.decryptBASE64(passwd));
			}
		}
		String driver = map.get("driver");

		String initialPoolSize = map.get("initialPoolSize");
		if (StringsUtils.isEmpty(initialPoolSize)
				|| !StringsUtils.isNumber(initialPoolSize)) {
			initialPoolSize = "2";
		}
		String minPoolSize = map.get("minPoolSize");
		if (StringsUtils.isEmpty(minPoolSize)
				|| !StringsUtils.isNumber(minPoolSize)) {
			minPoolSize = "2";
		}
		String maxPoolSize = map.get("maxPoolSize");
		if (StringsUtils.isEmpty(maxPoolSize)
				|| !StringsUtils.isNumber(maxPoolSize)) {
			maxPoolSize = "2";
		}
		String maxStatements = map.get("maxStatements");
		if (StringsUtils.isEmpty(maxStatements)
				|| !StringsUtils.isNumber(maxStatements)) {
			maxStatements = "2";
		}
		String maxIdleTime = map.get("maxIdleTime");
		if (StringsUtils.isEmpty(maxIdleTime)
				|| !StringsUtils.isNumber(maxIdleTime)) {
			maxIdleTime = "2";
		}

		BasicDataSource dataSource = null;
		try {
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(jdbcUrl);
			dataSource.setUsername(user);
			dataSource.setPassword(passwd);
			// 初始化连接数

			dataSource.setInitialSize(Integer.parseInt(initialPoolSize));

			// 最小空闲连接

			dataSource.setMinIdle(Integer.parseInt(minPoolSize));

			// 最大空闲连接

			dataSource.setMaxIdle(Integer.parseInt(maxPoolSize));

			// 超时回收时间(以毫秒为单位)

			dataSource.setMaxWait(Integer.parseInt(maxIdleTime));

			// 最大连接数

			dataSource.setMaxActive(Integer.parseInt(maxStatements));

			dataSource.setTestOnBorrow(true);// 调取连接时检查有效性

			dataSource.setTestOnReturn(true);

			dataSource.setTestWhileIdle(true);

			dataSource.setValidationQuery("select 1 from dual");

		} catch (Throwable e) {
			if (dataSource != null) {
				dataSource.close();
			}

			throw new Exception(e);
		}
		return dataSource;
	}

	public static DataSourceForDhcp getInstance(Map<String, String> param) {
		if (instance == null) {
			newInstance();
		}
		return instance;
	}

	synchronized private static void newInstance() {
		if (instance == null) {
			instance = new DataSourceForDhcp();
		}
	}

	public DataSource getDataSource(String code) throws Exception {
		return dsMap.get(code);
	}

	public DataSource getDataSource(String code, String driver, String jdbcUrl,
			String user, String passwd) throws Exception {
		FrameSqlDataSource ds = dsMap.get(code);
		if (ds != null) {
			return ds;
		}
		BasicDataSource dataSource = null;
		try {

			dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(jdbcUrl);
			dataSource.setUsername(user);
			dataSource.setPassword(passwd);

			// 初始化连接数

			dataSource.setInitialSize(3);

			// 最小空闲连接

			dataSource.setMinIdle(5);

			// 最大空闲连接

			dataSource.setMaxIdle(10);

			// 超时回收时间(以毫秒为单位)

			dataSource.setMaxWait(1000);

			// 最大连接数

			dataSource.setMaxActive(30);

			dataSource.setTestOnBorrow(true);// 调取连接时检查有效性

			dataSource.setTestOnReturn(true);

			dataSource.setTestWhileIdle(true);

			dataSource.setValidationQuery("select 1 from dual");
			FrameSqlDataSource d = new FrameSqlDataSource(dataSource);
			dsMap.put(code, d);
		} catch (Throwable e) {
			if (dataSource != null) {
				dataSource.close();
			}
			throw new Exception(e);
		}

		return dataSource;

	}

	public void shutdown() {
		for (FrameSqlDataSource ds : dsMap.values()) {
			try {
				if (ds.getParent() instanceof ComboPooledDataSource) {
					BasicDataSource new_name = (BasicDataSource) ds.getParent();

					new_name.close();

				}
			} catch (Throwable e) {

			}
		}
	}

	public static void main(String[] args) throws Exception {
		DataSource ds = DataSourceForDhcp.getInstance(null).getDataSource(
				"oracle");

		ResultSet rs = ds.getConnection().createStatement()
				.executeQuery("SELECT * FROM DURL");
		while (rs.next()) {
			System.out.println(rs.getObject(1));

		}

		log.debug(ds);
	}

	public void startup() {
		

	}

	public Map<String, String> getDataSourceMap(String code) throws Exception {
		Map<String, Map<String, String>> keyValues = DatasourceKeyValuesBean
				.getInstance().getKeyValues();
		return keyValues.get(code);
	}
}
