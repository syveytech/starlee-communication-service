package com.retail.starlee.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enstage.Encryptor;


public class Utility {
	private static Logger logger = LoggerFactory.getLogger(Utility.class.getName());

	public static String decrypt(String keyAlias, String encryptedStr) {
		String decryptedStr = "";
		try {
			Encryptor enc = new Encryptor(keyAlias);
			decryptedStr = enc.decrypt(encryptedStr);
			if (StringUtils.isBlank(decryptedStr)) {
				decryptedStr = encryptedStr;
			}
		} catch (Exception e) {
			logger.error("Exception :", e);
			return decryptedStr;
		}
		return decryptedStr;
	}

	public static String convertListToPreparedStatementClause(List<Integer> list) {
		String pstString = "";
		try {
			if (list != null && list.size() != 0) {
				for (int index = 0; index < list.size(); index++) {
					if (pstString.equals("")) {
						pstString = "?";
					} else {
						pstString += ",?";
					}
				}
			} else {
				throw new IllegalArgumentException("No values supplied.");
			}
		} catch (Exception e) {
			logger.warn("Exception in convertListToPreparedStatementClause :" + e);
		}
		return pstString;
	}

	public static List<String> stringToList(String str) {
		List<String> list = new ArrayList<String>();
		try {
			if (StringUtils.isBlank(str)) {
				logger.debug("str is null");
			}
			String strArr[] = str.split(",", -1);

			list = Arrays.asList(strArr);
		} catch (Exception e) {
			return list;
		}
		return list;
	}

	public static String mapToQueryString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();

		for (Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);

		return str;
	}

	public static String formatAmount(String amount) {
		int iLen = amount.length();

		if (iLen == 1) {
			String bal = "00" + amount;
			iLen = bal.length();
			return bal.substring(0, iLen - 2) + "." + bal.substring(iLen - 2);
		} else if (iLen == 2) {
			String bal = "0" + amount;
			iLen = bal.length();
			return bal.substring(0, iLen - 2) + "." + bal.substring(iLen - 2);
		} else {
			return amount.substring(0, iLen - 2) + "." + amount.substring(iLen - 2);
		}
	}

	public static String replaceString(String str, String str1, String str2) {
		if (str == null)
			return null;
		if (str1 == null)
			return str;
		if (str2 == null)
			return str;
		StringBuffer sbStr = new StringBuffer(str.trim());
		int str1length = str1.trim().length();
		int str2length = str2.trim().length();
		int index = 0;
		int fromIndex = -1;
		index = sbStr.toString().indexOf(str1, fromIndex);
		while (index >= 0) {
			sbStr.replace(index, index + str1length, str2);
			fromIndex = index + str2length;
			if (fromIndex <= sbStr.length())
				index = sbStr.toString().indexOf(str1, fromIndex);
			else
				index = -1;
		}
		return sbStr.toString();
	}

	public static String getCurrentDateTime(String format) {
		String currentDateTime = null;
		try {
			if (StringUtils.isBlank(format)) {
				logger.debug("Requested date format is null/blank");
				return null;
			}
			DateFormat sdf = new SimpleDateFormat(format);
			Date date = new Date();
			currentDateTime = sdf.format(date);

			logger.debug("currentDateTime :" + currentDateTime);
		} catch (Exception e) {
			logger.error("Error in getCurrentDateTime :", e);
			return null;
		}
		return currentDateTime;
	}

	public static int toInt(String str) {
		try {
			return (Integer.parseInt(str));
		} catch (Exception e) {
			return 0;
		}
	}
	public static String formatEventDateAndTime(String startDateAndTime, String endDateAndTime) {
        /*String startDate = "2017-12-30 13:50:00";
        String endDate = "2018-12-31 17:30:00";*/
        String eventDateAndTime = null;
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date1 = sdfSource.parse(startDateAndTime);
            Date date2 = sdfSource.parse(endDateAndTime);
           
            String dateLong1 = DateFormat.getDateInstance(DateFormat.LONG).format(date1);
            String dateLong2 = DateFormat.getDateInstance(DateFormat.LONG).format(date2);  
                
            String timeMedium1 = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(date1); 
            String timeMedium2 = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(date2); 
            
            eventDateAndTime = dateLong1 + " - " + dateLong2 + " | " + timeMedium1 + " to " + timeMedium2 + " IST";
        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
		return eventDateAndTime;
	}
}