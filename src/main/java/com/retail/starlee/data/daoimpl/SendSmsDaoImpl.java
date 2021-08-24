package com.retail.starlee.data.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.dao.ICommunicationInfoDao;
import com.retail.starlee.data.factory.ConnectionHelper;
import com.retail.starlee.models.UserDetail;

public class SendSmsDaoImpl implements ICommunicationInfoDao {
	private static Logger logger = LoggerFactory.getLogger(SendSmsDaoImpl.class.getName());

	@Override
	public List<UserDetail> getRecordsToSendSms(Connection con) {
		List<UserDetail> userDetailRecordList = new ArrayList<UserDetail>();
		UserDetail userDetail = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			//String selectQuery = "SELECT first_name, last_name, user_mobile_no from user_registration where user_mobile_no='8457846401'";
			String selectQuery = "SELECT first_name, user_mobile_no from customer";
			pst = con.prepareStatement(selectQuery);
			rs = pst.executeQuery();

			while (rs.next()) {
				userDetail = new UserDetail();
				userDetail.setFirstName((rs.getString("first_name")));
				//userDetail.setLastName((rs.getString("last_name")));
				userDetail.setUserMobileNumber((rs.getString("user_mobile_no")));
				userDetailRecordList.add(userDetail);
			}
		} catch (Exception e) {
			logger.error("Error in getRecordsToSendSms() method", e);
			userDetailRecordList = null;
		} finally {
			ConnectionHelper.closeResultSetPreparedStatementConnection(rs, pst,null);
		}
		return userDetailRecordList;
	}
}
