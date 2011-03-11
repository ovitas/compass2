/**
 * 
 */
package no.ovitas.compass2.kb.store.dao.jpa;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @class DataSourceConnectionFactory
 * @project compass2-kbstore-jpa
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.24.
 * 
 */
public class DataSourceConnectionFactory {

	private DataSource dateSource;

	private Log log = LogFactory.getLog(getClass());

	private Connection connection;

	public DataSourceConnectionFactory(DataSource dataSource) {
		dateSource = dataSource;

	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = dateSource.getConnection();
		}
		
		return connection;
	}
	
	public void commit() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.commit();
		}
	}
	
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

}
