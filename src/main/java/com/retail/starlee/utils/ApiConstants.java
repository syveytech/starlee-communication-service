package com.retail.starlee.utils;

public class ApiConstants {
	// Generic response Strings
	public static final String SUCCESS_RESPONSE = "{\"responseCode\":\"00\",\"responseMessage\":\"Success\"}";
	public final static String NO_VALID_INPUT_FIELDS_SENT = "{\"responseCode\":\"10\",\"responseMessage\":\"Please send valid input fields\"}";
	public final static String ERROR_WHILE_PARSING_INPUT = "{\"responseCode\":\"11\",\"responseMessage\":\"Invalid Json string\"}";
	//public final static String UNKNOWN_ERROR_RESPONSE = "{\"responseCode\":\"12\",\"responseMessage\":\"Unknown error occured\"}";
	public static final String ERROR_PARSING_RESPONSE = "{\"responseCode\":\"13\",\"responseMessage\":\"Error parsing the response\"}";
	public static final String SQL_ERROR = "{\"responseCode\":\"14\",\"responseMessage\":\"SQL Error\"}";
	public static final String INVALID_WIBMO_ACCOUNT_NUM = "{\"responseCode\":\"15\",\"responseMessage\":\"Invalid wibmo account number\"}";

	public final static String UNKNOWN_ERROR_RESPONSE = "Unknown error occured";
}