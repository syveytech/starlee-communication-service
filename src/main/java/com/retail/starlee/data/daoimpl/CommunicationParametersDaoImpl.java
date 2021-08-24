package com.retail.starlee.data.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.factory.ConnectionFactory;
import com.retail.starlee.data.factory.ConnectionHelper;
import com.retail.starlee.utils.PropertyUtility;

public class CommunicationParametersDaoImpl{
	static Logger logger = LoggerFactory.getLogger(CommunicationParametersDaoImpl.class);
	private static HashMap<String, String> parameterMap = new HashMap<String, String>();

	static {
		try {
			load();
		} catch (Exception e) {
			logger.error("Exception : ",e);
		}
	}

	public static synchronized void load() {
		logger.debug("Initializing CommunicationParameters");

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String paramCode = "";
		String paramValue = "";

		try {
			con = ConnectionFactory.createConnection();
			String query = "select * from parameters";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				paramCode = rs.getString("parameter_name");
				paramValue = rs.getString("parameter_value");
				parameterMap.put(paramCode, paramValue);
			}
			logger.debug("Loaded CommunicationParameters");
		} catch (Exception e) {
			logger.error("Error :", e);
		} finally {
			ConnectionHelper.closeResultSetPreparedStatementConnection(rs, pst, con);
		}
	}

	public static String getParameter(String paramCode) {
		String paramValue = "";
		try {
			if (null == parameterMap) {
				load();
			}
			//Added to enable Beta testing
			String mode = "";
			Properties prop = PropertyUtility.getProperties();
			if(prop != null){
				mode = prop.getProperty("APPLICATION_MODE");
				if (StringUtils.isNotBlank(mode)) {
					paramCode = mode + "_" + paramCode;
				}
			}
			if (StringUtils.isNotBlank(paramCode)) {
				paramValue = parameterMap.get(paramCode);
			}
		} catch (Exception e) {
			logger.debug("Error: ", e);
			return null;
		}
		return paramValue;
	}

	public static String getParameter(String paramId, int programId) {
		String paramValue = "";
		try {
			if (null == parameterMap) {
				load();
			}

			if (programId != 0) {
				paramId = programId + "_" + paramId;
			}
			if (StringUtils.isNotBlank(paramId)) {
				paramValue = parameterMap.get(paramId);
			}
		} catch (Exception e) {
			logger.debug("Error: ", e);
			return null;
		}
		return paramValue;
	}
}
