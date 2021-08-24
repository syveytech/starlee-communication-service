
package com.retail.starlee.data.daoimpl;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.retail.starlee.data.dao.IServiceProvider;
import com.retail.starlee.models.UserDetail;
import com.retail.starlee.utils.ApiConstants;
import com.retail.starlee.utils.HttpUtil;


public class BulkSmsImpl implements IServiceProvider{
    static Logger logger = LoggerFactory.getLogger(BulkSmsImpl.class);

    public String sendRequestToMsp(String posturl, String request) throws IOException {
	String response = null;
	boolean isHttpsProtocol = false;
	try {
	    String proxyIp = CommunicationParametersDaoImpl.getParameter("PROXY_IP");
	    String proxyPort = CommunicationParametersDaoImpl.getParameter("PROXY_PORT");
	    int connectionTimeoutInSec = Integer.parseInt(CommunicationParametersDaoImpl.getParameter("SP_CONNECTION_TIME_OUT"));
	    int readTimeOutInSec = Integer.parseInt(CommunicationParametersDaoImpl.getParameter("SP_READ_TIME_OUT"));

	    try {
		HttpUtil.init(proxyIp, proxyPort);
	    } catch (Exception e) {
		logger.error("Exception in HttpUtil.init :", e);
	    }
	    byte[] postData = request.getBytes("UTF-8");
	    URL reqUrl = new URL(posturl);

	    if ("https".equals(reqUrl.getProtocol())) {
		isHttpsProtocol = true;
	    }
	    response = HttpUtil.postData(posturl, postData, false, HttpUtil.JSON, false, connectionTimeoutInSec, readTimeOutInSec);
	} catch (Exception e) {
	    logger.error("Exception in sendRequest :", e);
	    response = null;
	}
	return response;
    }

	@Override
	public String sendPromotionalSms(UserDetail userDetail, String smsContent)
			throws Exception {
		try {
			if (userDetail != null && smsContent != null) {
				logger.debug("UserDetails: " + userDetail.toString());
				logger.debug("smsContent: " + smsContent);
				String request = "{}";
				String bulkSmsUrl = CommunicationParametersDaoImpl.getParameter("BULKSMS_API_URL");
				String bulkSmsApiKey = CommunicationParametersDaoImpl.getParameter("BULKSMS_API_KEY");
				String bulkSmsApiToken = CommunicationParametersDaoImpl.getParameter("BULKSMS_API_TOKEN");
				String bulkSmsSender = CommunicationParametersDaoImpl.getParameter("BULKSMS_SENDER");
				String bulkSmsReceiver = userDetail.getUserMobileNumber();
				String buldSmsMsgType = CommunicationParametersDaoImpl.getParameter("BULKSMS_MSG_TYPE");
				
				/*bulkSmsUrl = bulkSmsUrl.replaceAll("%", bulkSmsApiKey);
				bulkSmsUrl = bulkSmsUrl.replaceAll("#", bulkSmsApiToken);
				bulkSmsUrl = bulkSmsUrl.replaceAll("~", bulkSmsSender);
				bulkSmsUrl = bulkSmsUrl.replaceAll("@", bulkSmsReceiver);
				bulkSmsUrl = bulkSmsUrl.replaceAll("!", buldSmsMsgType);
				bulkSmsUrl = bulkSmsUrl.replaceAll("-", smsContent);*/
				
				bulkSmsUrl = bulkSmsUrl.concat("api_key=" + URLEncoder.encode(bulkSmsApiKey, "UTF-8") + "&");
				bulkSmsUrl = bulkSmsUrl.concat("api_token=" + URLEncoder.encode(bulkSmsApiToken, "UTF-8") + "&");
				bulkSmsUrl = bulkSmsUrl.concat("sender=" + URLEncoder.encode(bulkSmsSender, "UTF-8") + "&");
				bulkSmsUrl = bulkSmsUrl.concat("receiver=" + URLEncoder.encode(bulkSmsReceiver, "UTF-8") + "&");
				bulkSmsUrl = bulkSmsUrl.concat("msgtype=" + URLEncoder.encode(buldSmsMsgType, "UTF-8") + "&");
				bulkSmsUrl = bulkSmsUrl.concat("sms=" + URLEncoder.encode(smsContent, "UTF-8"));
				
				logger.debug("BulkSms API Url for sending sms: " + bulkSmsUrl);
				
				String response = sendRequestToMsp(bulkSmsUrl, request);
				logger.debug("BulkSms API response: " + response);
				if (StringUtils.isNotBlank(response)) {
					logger.debug("Response is not blank");
					// return new AppsFlySearchApiResponse().toPojo(response);
					return response;
				} else {
					logger.debug("Throwing UnknownError");
					 throw new UnknownError(ApiConstants.UNKNOWN_ERROR_RESPONSE);
				}
			} else {
				logger.debug("Throwing UnknownError");
				throw new UnknownError(ApiConstants.UNKNOWN_ERROR_RESPONSE);
			}
		} catch (Exception e) {
			logger.debug("Error in getPopularProduct: ", e);
			throw e;
		}
	}
}
