package com.dotoyo.dsform.datasource;

import java.sql.Connection;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

public class FrameSqlSessionFactory implements SqlSessionFactory{
	private SqlSessionFactory f=null;
	public FrameSqlSessionFactory(SqlSessionFactory f){
		this.f=f;
	}
	@Override
	public Configuration getConfiguration() {
		return f.getConfiguration();
	}

	@Override
	public SqlSession openSession() {
		
		return f.openSession();
	}

	@Override
	public SqlSession openSession(boolean arg0) {
		
		return f.openSession(arg0);
	}

	@Override
	public SqlSession openSession(Connection arg0) {
		
		return f.openSession(arg0);
	}

	@Override
	public SqlSession openSession(TransactionIsolationLevel arg0) {
		return f.openSession(arg0);
	}

	@Override
	public SqlSession openSession(ExecutorType arg0) {
		return f.openSession(arg0);
	}

	@Override
	public SqlSession openSession(ExecutorType arg0, boolean arg1) {
		
		return f.openSession(arg0,arg1);
	}

	@Override
	public SqlSession openSession(ExecutorType arg0,
			TransactionIsolationLevel arg1) {
		
		return f.openSession(arg0,arg1);
	}

	@Override
	public SqlSession openSession(ExecutorType arg0, Connection arg1) {
	
		return f.openSession(arg0, arg1);
	}

}
