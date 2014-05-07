package com.feedback.bl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;

public class DbConnection {
	private static final String dbDriver = "com.mysql.jdbc.Driver";

	private static String dbUrl = "jdbc:mysql://127.0.0.1/";

	private static final String dbUser = "root";

	private static final String dbPassword = "1234";

	public Connection connection = null;

	public Configuration configuration = null;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public DbConnection(String dbName) {

		try {
			Class.forName(dbDriver);
			connection = DriverManager.getConnection(dbUrl + dbName, dbUser,
					dbPassword);

			// Create the SessionFactory from hibernate.cfg.xml
			Configuration c = new Configuration();
			c.setProperty("hibernate.connection.url", dbUrl + dbName);
			configuration = c.configure();
		} catch (HibernateException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
