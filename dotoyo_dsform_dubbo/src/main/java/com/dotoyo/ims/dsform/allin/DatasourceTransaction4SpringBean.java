
package com.dotoyo.ims.dsform.allin;

import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.dotoyo.dsform.log.LogProxy;

public class DatasourceTransaction4SpringBean extends
		DataSourceTransactionManager {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final transient IFrameLog log = new LogProxy(
			LogFactory.getLog(DatasourceTransaction4SpringBean.class));

	@Override
	public DataSource getDataSource() {
		return super.getDataSource();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
