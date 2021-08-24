package com.retail.starlee.application.manager;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.business.email.SmsPromotionProcessor;
import com.retail.starlee.business.email.SmsPromotionProcessorBulkSmsPlans;
import com.retail.starlee.data.daoimpl.CommunicationParametersDaoImpl;
import com.retail.starlee.data.daoimpl.EmailSmsNotificationRecordsDaoImpl;
import com.retail.starlee.models.UserDetail;

/**
 * Sending EMAIL and SMS.
 * @author Manoj
 */
public class AsyncNotificationManager {
	static Logger logger = LoggerFactory.getLogger(AsyncNotificationManager.class.getName());
	
	public boolean sendPromotionsThroughSms(UserDetail userDetail) {
		Boolean isSmsSent = false;
		try {
			 ExecutorService executor = Executors.newFixedThreadPool(1);
			 FutureTask<Boolean> smsThread = new FutureTask<Boolean>(new SmsPromotionProcessorBulkSmsPlans(userDetail));
			 logger.debug("Async send Promotions through sms started.");
			 executor.execute(smsThread);
			 logger.debug("Async send Promotions through sms completed.");
			 executor.shutdown();
			 isSmsSent = smsThread.get();
			 logger.debug("Is Promotions Sent Through sms:" + isSmsSent);
			 return isSmsSent;
		} catch (CancellationException c) {
			isSmsSent = false;
		    logger.debug("CancellationException:", c);
		} catch (InterruptedException i) {
			isSmsSent = false;
		    logger.debug("InterruptedException:", i);
		} catch (Exception e) {
			logger.error("Error in AsyncNotificationManager",e);
			isSmsSent = false;
		}
		return isSmsSent;	
	}	

	public boolean sendSmsToUser(UserDetail userDetail) {
		boolean smsFlag = false, isSmsSent = false;
		try {
			//emailFlag = Integer.parseInt(CommunicationParametersDaoImpl.getParameter("IS_SEND_EMAIL_ACTIVE"))==1?true:false;
			smsFlag = Integer.parseInt(CommunicationParametersDaoImpl.getParameter("IS_SEND_SMS_ACTIVE"))==1?true:false;
			
			if (smsFlag) {
				logger.debug("smsFlag::" + smsFlag);
				isSmsSent = sendPromotionsThroughSms(userDetail);
				
				//update data to email_sms_notification_records table asynchronously
				if (isSmsSent) {
					new EmailSmsNotificationRecordsDaoImpl().logIsSmsSentRecordAsyncronously(userDetail,1);
				} else {
					new EmailSmsNotificationRecordsDaoImpl().logIsSmsSentRecordAsyncronously(userDetail,0);
				}				
			} 		
		} catch (Exception e) {
			logger.error("Error in sending promotional sms",e);
			isSmsSent = false;
		}
		return isSmsSent;
	}
}