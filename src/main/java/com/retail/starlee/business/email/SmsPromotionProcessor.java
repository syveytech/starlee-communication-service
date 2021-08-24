package com.retail.starlee.business.email;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enstage.alert.Alert;
import com.enstage.messageserver.EnMessage;
import com.enstage.messageserver.MessageServerAPI;
import com.enstage.messageserver.RcpPhone;
import com.enstage.messageserver.SMSMessage;
import com.retail.starlee.data.daoimpl.CommunicationParametersDaoImpl;
import com.retail.starlee.models.UserDetail;
import com.retail.starlee.utils.Utility;

public class SmsPromotionProcessor implements Callable<Boolean> {
	static Logger logger = LoggerFactory.getLogger(SmsPromotionProcessor.class.getName());
	private UserDetail userDetail;
	private final static String RETRY_COUNT = CommunicationParametersDaoImpl.getParameter("SMS_RETRY_COUNT");
	
	public SmsPromotionProcessor(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	
	public boolean sendPromotionThroughSms(UserDetail userDetail) {
		String smsFrom = CommunicationParametersDaoImpl.getParameter("SMS_FROM");
		String smsPromotionTemplate = CommunicationParametersDaoImpl.getParameter("SMS_PROMOTIONS_TEMPLATE");
		
		HashMap<String, String> alertMap =  null;
		int count = Integer.parseInt(RETRY_COUNT);
		boolean isSmsSent = false;
		try {
			alertMap = setAlertMapForSms(userDetail);
			
			//Retry logic
			int i;
			if (count > 0) {
				for (i = 1; i <= count; i++) {
					isSmsSent = sendSms(alertMap, smsPromotionTemplate, smsFrom,userDetail.getUserMobileNumber());
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
	
	public HashMap<String, String> setAlertMapForSms(UserDetail userDetail) {
		HashMap<String, String> hm = new HashMap<String, String>();
		try {
			hm.put("<CUSTOMER>", userDetail.getFirstName());
		} catch (Exception e) {
			logger.error("Exception :", e);
			return null;
		}
		return hm;
	}
	
	public static boolean sendSms(HashMap<String, String> alertMap, String alertText, String smsFrom, String customerMobile) {
		boolean result = false;
		String smsText = alertText;
		try {
			logger.debug("Sending SMS to mobile: "+customerMobile);
			Alert alert = new Alert();
			if (null != customerMobile) {
				final String smsHost = CommunicationParametersDaoImpl.getParameter("SMS_HOST");
				final String smsPort = CommunicationParametersDaoImpl.getParameter("SMS_PORT");
				if (StringUtils.isBlank(alertText)) {
					logger.debug("alertText was null");
					return false;
				}
				alert.setSmsMap(alertMap);
				alert.setMobile(customerMobile);
				alert.setSmsFrom(smsFrom);

				for (String key : alertMap.keySet()) {
					String data = alertMap.get(key);
					alertText = Utility.replaceString(alertText, key, data);
				}
				
				//if alertText length is bigger then 160
				int alertTextCharLength = alertText.length();
				if(alertTextCharLength >= 160){
					logger.debug("SMS content length is bigger then 160 character - length of sms is - "+alertTextCharLength);
					for (String key : alertMap.keySet()) {
						String data = alertMap.get(key);
						if(key == "<EVENT_NAME>"){
							int x = alertTextCharLength - 160;
							int eventNameCharLength = data.length();
							x = eventNameCharLength-x;
							data = (String) data.subSequence(0,x);
						}
						smsText = Utility.replaceString(smsText, key, data);
					}
					alertText = smsText;
				}
				logger.debug("SMS Details - SMS FROM :"+smsFrom+", To customerMobile :"+customerMobile+", SMSText is :"+alertText);

				EnMessage enM = new EnMessage();
				RcpPhone rcpPhone = new RcpPhone();
				rcpPhone.setNumber(alert.getMobile());
				SMSMessage smsMessage = new SMSMessage();
				smsMessage.setSMSType("SMS");
				smsMessage.setRcpPhone(rcpPhone);
				smsMessage.setFrom(alert.getSmsFrom());
				smsMessage.setBody(alertText);
				enM.setSMSMessage(smsMessage);
				enM.setRefId(123);
				EnMessage enMessageRes = MessageServerAPI.sendMessage(enM, smsHost, Integer.parseInt(smsPort));
				logger.debug("SMS SuccessCode : " + enMessageRes.getSuccessCode());
				if (enMessageRes.getSuccessCode().equals("00")) {
					result = true;
				}
			} else {
				logger.debug("No mobile number");
				result  = false;
			}
		} catch (Exception e) {
			logger.error("Exception in sending SMS :", e);
			result = false;
		}
		return result;
	}

	@Override
	public Boolean call() throws Exception {
		logger.debug("Sending Promotions through SMS");
		return sendPromotionThroughSms(userDetail);
	}
}
