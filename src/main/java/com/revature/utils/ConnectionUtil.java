package com.revature.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	private static ConnectionUtil cu = null;
	private static Properties prop;
	private static Connection conn;
	//private static String url = "jdbc:mysql://localhost:3306/p0";
	//private static String username = "root";
	//private static String password = "password";

		/**
		 * This method should read in the "database.properties" file and load the values
		 * into the Properties variable
		 */
		private ConnectionUtil() {

			try (InputStream input = new FileInputStream("/src/main/resources/database.properties")) {
				Properties prop = new Properties();
				prop.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		public static synchronized ConnectionUtil getConnectionUtil() {
			if (cu == null)
				cu = new ConnectionUtil();
			return cu;

		}

		/**
		 * This method should create and return a Connection object
		 * 
		 * @return a Connection to the database
		 */
		public static Connection getConnection() throws SQLException {
			// Hint: use the Properties variable to setup your Connection object
			
			try {
				conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/p0", "root", "password");
				//conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("usr"), prop.getProperty("pswd"));
				return conn;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;

		}
		
	}