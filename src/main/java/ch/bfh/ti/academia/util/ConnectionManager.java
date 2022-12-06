/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The class ConnectionManager is used to manage database connections.
 */
public class ConnectionManager {

	private static final String PROPERTY_FILE_PATH = "/jdbc.properties";

	private static final String URL;
	private static final String USER;
	private static final String PASSWORD;

	static {
		try {
			Properties props = new Properties();
			props.load(ConnectionManager.class.getResourceAsStream(PROPERTY_FILE_PATH));
			Class.forName(props.getProperty("database.driver"));
			URL = props.getProperty("database.url");
			USER = props.getProperty("database.user");
			PASSWORD = props.getProperty("database.password");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Connection getConnection(boolean autoCommit) {
		try {
			Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
			connection.setAutoCommit(autoCommit);
			return connection;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void commit(Connection connection) {
		try {
			connection.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
