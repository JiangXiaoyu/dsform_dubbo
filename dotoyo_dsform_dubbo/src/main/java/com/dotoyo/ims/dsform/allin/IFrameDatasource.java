package com.dotoyo.ims.dsform.allin;

import java.util.Map;

import javax.sql.DataSource;

/**
 * 数据源接口类
 * 
 * @author xieshh
 */
public interface IFrameDatasource {
	/**
	 * 取数据源
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public DataSource getDataSource(String code) throws Exception;

	/**
	 * 取数据源
	 * 
	 * @param code
	 * @param driver
	 * @param jdbcUrl
	 * @param user
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public DataSource getDataSource(String code, String driver, String jdbcUrl,
			String user, String passwd) throws Exception;

	/**
	 * 根据数据源编码取数据源配置信息
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getDataSourceMap(String code) throws Exception;
}
