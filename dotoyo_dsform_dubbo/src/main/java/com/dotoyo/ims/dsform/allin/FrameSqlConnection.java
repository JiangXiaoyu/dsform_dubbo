package com.dotoyo.ims.dsform.allin;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class FrameSqlConnection implements java.sql.Connection {

	private java.sql.Connection pConn = null;
	private FrameSqlConnectionMonitor m = new FrameSqlConnectionMonitor(this);

	public FrameSqlConnection(java.sql.Connection pConn) {

		try {
			IFrameSystemMonitor moF;
			moF = FrameFactory.getSystemMonitorFactory(null);
			moF.addMonitor(m);
		} catch (Throwable e) {

		}

		this.pConn = pConn;
	}

	public Statement createStatement() throws SQLException {
		Statement st = pConn.createStatement();

		return st;
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql);
		return st;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		CallableStatement st = pConn.prepareCall(sql);
		return st;
	}

	public String nativeSQL(String sql) throws SQLException {
		String st = pConn.nativeSQL(sql);
		return st;
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		pConn.setAutoCommit(autoCommit);

	}

	public boolean getAutoCommit() throws SQLException {
		boolean st = pConn.getAutoCommit();
		return st;
	}

	public void commit() throws SQLException {
		pConn.commit();

	}

	public void rollback() throws SQLException {
		pConn.rollback();

	}

	public void close() throws SQLException {
		try {
			IFrameSystemMonitor moF;
			moF = FrameFactory.getSystemMonitorFactory(null);
			moF.remoteMonitor(m);
		} catch (Throwable e) {

		}finally{
			pConn.close();
		}
		

	}

	public boolean isClosed() throws SQLException {
		boolean st = pConn.isClosed();
		return st;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		DatabaseMetaData st = pConn.getMetaData();
		return st;
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		pConn.setReadOnly(readOnly);

	}

	public boolean isReadOnly() throws SQLException {
		boolean st = pConn.isReadOnly();
		return st;
	}

	public void setCatalog(String catalog) throws SQLException {
		pConn.setCatalog(catalog);

	}

	public String getCatalog() throws SQLException {
		String st = pConn.getCatalog();
		return st;
	}

	public void setTransactionIsolation(int level) throws SQLException {
		pConn.setTransactionIsolation(level);

	}

	public int getTransactionIsolation() throws SQLException {
		int st = pConn.getTransactionIsolation();
		return st;
	}

	public SQLWarning getWarnings() throws SQLException {
		SQLWarning st = pConn.getWarnings();
		return st;
	}

	public void clearWarnings() throws SQLException {
		pConn.clearWarnings();

	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Statement st = pConn.createStatement();
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
		return st;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		CallableStatement st = pConn.prepareCall(sql, resultSetType,
				resultSetConcurrency);
		return st;
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		Map<String, Class<?>> st = pConn.getTypeMap();
		return st;
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		pConn.setTypeMap(map);

	}

	public void setHoldability(int holdability) throws SQLException {
		pConn.setHoldability(holdability);

	}

	public int getHoldability() throws SQLException {
		int st = pConn.getHoldability();
		return st;
	}

	public Savepoint setSavepoint() throws SQLException {
		Savepoint st = pConn.setSavepoint();
		return st;
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		Savepoint st = pConn.setSavepoint();
		return st;
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		pConn.rollback(savepoint);

	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		pConn.releaseSavepoint(savepoint);

	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Statement st = pConn.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
		return st;

	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		CallableStatement st = pConn.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
		return st;
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql, autoGeneratedKeys);
		return st;

	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql, columnIndexes);
		return st;

	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		PreparedStatement st = pConn.prepareStatement(sql, columnNames);
		return st;

	}

	
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		Array st = pConn.createArrayOf(typeName, elements);
		return st;
	}

	
	public Blob createBlob() throws SQLException {
		Blob st = pConn.createBlob( );
		return st;
	}

	
	public Clob createClob() throws SQLException {
		Clob st = pConn.createClob( );
		return st;
	}

	
	public NClob createNClob() throws SQLException {
		NClob st = pConn.createNClob( );
		return st;
	}

	
	public SQLXML createSQLXML() throws SQLException {
		SQLXML st = pConn.createSQLXML( );
		return st;
	}

	
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		Struct st = pConn.createStruct( typeName,attributes);
		return st;
	}

	
	public Properties getClientInfo() throws SQLException {
		Properties st = pConn.getClientInfo( );
		return st;
	}

	
	public String getClientInfo(String name) throws SQLException {
		String st = pConn.getClientInfo( name);
		return st;
	}

	
	public boolean isValid(int timeout) throws SQLException {
		boolean st = pConn.isValid( timeout);
		return st;
	}

	
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		  pConn.setClientInfo( properties);
		
	}

	
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		pConn.setClientInfo(name,value );
		
		
	}

	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		boolean st = pConn.isWrapperFor( iface);
		return st;
	}

	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		T st = pConn.unwrap( iface);
		return st;
	}

	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
