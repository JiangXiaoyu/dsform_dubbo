package com.dotoyo.dsform.datasource;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;

import com.dotoyo.ims.dsform.allin.FrameFactory;
import com.dotoyo.ims.dsform.allin.FrameUtils;
import com.dotoyo.ims.dsform.allin.IFrameLog;
import com.dotoyo.dsform.log.LogProxy;

/**
 * ��ܶ�Spring���֧��
 * 
 * @author xieshh
 * 
 */
public class SpringDataSourceBean implements DataSource {
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(SpringDataSourceBean.class));
	private String code = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private SpringDataSourceBean() {
		//log.debug("1");
	}

	public Connection getConnection() throws SQLException {

		try {
			DataSource ds = FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode());
			if (ds == null) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			Connection con = ds.getConnection();
			return con;
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			return FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode())
					.getConnection(username, password);
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}
	}

	public PrintWriter getLogWriter() throws SQLException {
		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			return FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode()).getLogWriter();
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			FrameFactory.getDataSourceFactory(null).getDataSource(getDsCode())
					.setLogWriter(out);
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			FrameFactory.getDataSourceFactory(null).getDataSource(getDsCode())
					.setLoginTimeout(seconds);
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}

	}

	public int getLoginTimeout() throws SQLException {
		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			return FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode()).getLoginTimeout();
		} catch (SQLException e) {
			throw e;
		} catch (Throwable e) {
			throw new SQLException(e.getMessage());
		}
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {

		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			DataSource pDs = FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode());
			Class<?> cls = pDs.getClass();

			Method method = null;
			method = cls.getDeclaredMethod("unwrap",
					new Class[] { Class.class });
			@SuppressWarnings("unchecked")
			T st = (T) method.invoke(pDs, new Object[] { iface });
			return st;
		} catch (Throwable e) {
			throw new SQLException("unwrap");
		}

	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {

		try {
			if (FrameFactory.getDataSourceFactory(null).getDataSource(
					getDsCode()) == null) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("code", getDsCode());
				throw new SQLException(FrameUtils.getWords("3000000000000029",
						"", map));
			}
			DataSource pDs = FrameFactory.getDataSourceFactory(null)
					.getDataSource(getDsCode());
			Class<?> cls = pDs.getClass();
			Method method = cls.getDeclaredMethod("isWrapperFor",
					new Class[] { Class.class });

			Boolean st = (Boolean) method.invoke(pDs, new Object[] { iface });
			return st;
		} catch (Throwable e) {
			throw new SQLException("isWrapperFor");
		}
	}

	protected String getDsCode() throws SQLException {
		String sub = "";
		
		return String.format("%s%s", code, sub);
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
