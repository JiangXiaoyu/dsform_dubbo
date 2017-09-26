package com.dotoyo.ims.dsform.allin;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class FrameSqlDataSource implements DataSource {
	private DataSource pDs = null;

	public DataSource getParent() {
		return pDs;
	}

	public FrameSqlDataSource(DataSource pDs) {
		this.pDs = pDs;
	}

	public Connection getConnection() throws SQLException {
		Connection st = pDs.getConnection();
		FrameSqlConnection conn = new FrameSqlConnection(st);
		return conn;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection st = pDs.getConnection(username, password);
		FrameSqlConnection conn = new FrameSqlConnection(st);
		return conn;
	}

	public PrintWriter getLogWriter() throws SQLException {
		PrintWriter st = pDs.getLogWriter();
		return st;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		pDs.setLogWriter(out);

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		pDs.setLoginTimeout(seconds);

	}

	public int getLoginTimeout() throws SQLException {
		int st = pDs.getLoginTimeout();
		return st;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {

		try {
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
			Class<?> cls = pDs.getClass();
			Method method = cls.getDeclaredMethod("isWrapperFor",
					new Class[] { Class.class });

			Boolean st = (Boolean) method.invoke(pDs, new Object[] { iface });
			return st;
		} catch (Throwable e) {
			throw new SQLException("isWrapperFor");
		}
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
