package com.retail.starlee.data.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.utils.PropertyUtility;
import com.retail.starlee.utils.Utility;

public class ConnectionFactory {
	private static Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
	private static final int DEFAULT_TIMEOUT_FOR_ISVALIDDBCONNECTION_TEST = 5;

	public static Connection createConnectionForJob(JobDataMap dataMap, String connectionName) {
		Connection con = null;
		try {
			logger.debug("Checking connection for :" + connectionName);
			con = (Connection) dataMap.get(connectionName);
			if (con == null || !con.isValid(DEFAULT_TIMEOUT_FOR_ISVALIDDBCONNECTION_TEST)) {
				logger.debug("Connection is not valid, creating new DB Connection");
				con = getDBConnectionFromProperties();
			}
		} catch (SQLException e) {
			logger.debug("Exception in isConnectionValid " + e.getMessage());
		}
		return con;
	}

	private static Connection getDBConnectionFromProperties() {
		// TODO Auto-generated method stub
		Properties config = PropertyUtility.getProperties();
		Connection con = null;
		try {
			Class.forName(config.getProperty("JDBC_DRIVER"));
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());

			String passPhrase = config.getProperty("ENCRYPTION_KEY_ALIAS");
			String dbPwdEnc = config.getProperty("DB_PASSWORD");

			String dbPwd = Utility.decrypt(passPhrase, dbPwdEnc);
			con = DriverManager.getConnection(config.getProperty("DB_URL"), config.getProperty("DB_USERNAME"), dbPwd);
		} catch (Exception e) {
			logger.error("Exception :", e);
		}
		return con;
	}

	public static Connection createConnection() {
		return getDBConnectionFromProperties();
	}
}
