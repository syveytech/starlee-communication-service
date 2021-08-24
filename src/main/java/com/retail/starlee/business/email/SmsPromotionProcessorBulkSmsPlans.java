package com.retail.starlee.business.email;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.daoimpl.BulkSmsImpl;
import com.retail.starlee.data.daoimpl.CommunicationParametersDaoImpl;
import com.retail.starlee.models.UserDetail;

public class SmsPromotionProcessorBulkSmsPlans implements Callable<Boolean> {
	static Logger logger = LoggerFactory.getLogger(SmsPromotionProcessorBulkSmsPlans.class.getName());
	private UserDetail userDetail;
	private final static String RETRY_COUNT = CommunicationParametersDaoImpl.getParameter("SMS_RETRY_COUNT");
	
	public SmsPromotionProcessorBulkSmsPlans(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	
	public boolean sendPromotionThroughSms(UserDetail userDetail) {
		
		String smsPromotionTemplate = CommunicationParametersDaoImpl.getParameter("SMS_PROMOTIONS_TEMPLATE");
		
		String smsContent =  null;
		int count = Integer.parseInt(RETRY_COUNT);
		boolean isSmsSent = false;
		try {
			smsContent = smsPromotionTemplate.replaceAll("customer", userDetail.getFirstName());
			
			//Retry logic
			int i;
			if (count > 0) {
				for (i = 1; i <= count; i++) {
					isSmsSent = sendSms(userDetail, smsContent);
					if (isSmsSent) {
						logger.debug("SMS sent successfully, retry count=" + i);
						return isSmsSent;
					}
				}
				logger.debug("All attempts to send sms failed, retry count=" + (i - 1));
			} else {
				logger.warn("Invalid count number, count=" + count);
			}
			
		} catch (Exception e) {
			logger.error("Error in sendEventTicketThroughSms",e);
			isSmsSent = false;
		}
		return isSmsSent;
		
	}
	
	
	public static boolean sendSms(UserDetail userDetail, String smsContent) {
		BulkSmsImpl bulkSmsImpl = new BulkSmsImpl();
		try {
			bulkSmsImpl.sendPromotionalSms(userDetail, smsContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Boolean call() throws Exception {
		logger.debug("Sending Promotions through SMS");
		return sendPromotionThroughSms(userDetail);
	}
}
