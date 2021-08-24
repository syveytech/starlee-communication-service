package com.retail.starlee.data.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.factory.ConnectionFactory;
import com.retail.starlee.data.factory.ConnectionHelper;
import com.retail.starlee.models.UserDetail;

public class EmailSmsNotificationRecordsDaoImpl {
	static Logger logger = LoggerFactory.getLogger(EmailSmsNotificationRecordsDaoImpl.class);
	
	public void updateData(final UserDetail userDetail, final int isSmsSent) {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String insertQuery = "insert into email_sms_notification_records_test (first_name,user_mobile_no,is_sms_sent) values(?,?,?)";
		int count ;
		try {
			con = ConnectionFactory.createConnection();
			preparedStatement = con.prepareStatement(insertQuery);
			preparedStatement.setString(1, userDetail.getFirstName());
			preparedStatement.setString(1, userDetail.getUserMobileNumber());
			preparedStatement.setInt(2, isSmsSent);
			count = preparedStatement.executeUpdate();
			if (count > 0)
				logger.debug("updated data into email_sms_notification_records");
			else 
				logger.debug("Failed to update data into email_sms_notification_records");
		} catch (Exception e) {
			logger.error("Error in EmailSmsNotificationRecordsDaoImpl during updating data",e);
		} finally {
			ConnectionHelper.closeResultSetPreparedStatementConnection(null, preparedStatement,con);
		}
	}
	
	public void logIsSmsSentRecordAsyncronously(final UserDetail userDetail, final int isSmsSent) {
		try {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						updateData(userDetail, isSmsSent);
					} catch (Exception e) {
						logger.error("Error in child thread during saveData",e);
					}
				}
			};
			t.start();
		} catch (Exception e) {
			logger.error("Error in saveDataAsyncronously",e);
		}
	}
}
