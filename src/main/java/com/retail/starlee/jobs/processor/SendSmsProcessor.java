package com.retail.starlee.jobs.processor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.application.manager.AsyncNotificationManager;
import com.retail.starlee.data.daoimpl.SendSmsDaoImpl;
import com.retail.starlee.data.factory.ConnectionFactory;
import com.retail.starlee.models.UserDetail;

public class SendSmsProcessor implements Job {
	private static Logger logger = LoggerFactory.getLogger(SendSmsProcessor.class.getName());
	private static final int DEFAULT_TIMEOUT_FOR_ISVALIDDBCONNECTION_TEST = 5;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Starting the job SendSms processor for sending sms");
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Connection con = (Connection) dataMap.get("connectionForSendSmsJob");
		
		try {
			if (con == null || !con.isValid(DEFAULT_TIMEOUT_FOR_ISVALIDDBCONNECTION_TEST)) {
				logger.debug("Connection is not valid, creating new DB Connection");
				con = ConnectionFactory.createConnection();
				context.getJobDetail().getJobDataMap().put("connectionForSendSmsJob", con);// in case a new connection was created
			}
		} catch (SQLException e) {
			logger.debug("Exception in isConnectionValid " + e.getMessage());
		}
		sendSms(con);
		logger.debug("Send Sms processor Job Over");
	}
	
	private void sendSms(Connection con) {
		boolean isSmsSent = false;
		SendSmsDaoImpl sendSmsDaoImpl = new SendSmsDaoImpl();
		try {
			List<UserDetail> userDetailsList = sendSmsDaoImpl.getRecordsToSendSms(con);
			if (userDetailsList != null && !userDetailsList.isEmpty()) {
				logger.debug("Total number of records to send Sms: " + userDetailsList.size());
				for (UserDetail data : userDetailsList) {
					logger.debug("Sending sms for the user: " +data.getUserName() + ":mobile number: " + data.getUserMobileNumber());
					try {
						isSmsSent = new AsyncNotificationManager().sendSmsToUser(data);
					} catch (Exception e) {
						logger.error("Error in sendEmailAndSMS",e);
						isSmsSent = false;
					}
				}
			} else {
				logger.debug("No records found for send Sms job");
			}
		} catch (Exception e) {
			logger.error("Error in sendSms",e);
		}	
	}
}
