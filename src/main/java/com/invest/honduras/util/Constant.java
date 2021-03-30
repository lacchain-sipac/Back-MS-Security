package com.invest.honduras.util;

public class Constant {
	
	public static final long TIME_VALUE_TOKEN = 120;
	public static final long TIME_VALUE_SESSION = 120;

	public static final long TIME_VALUE_TOKEN_AUTO_LOGIN = 2880;
	public static final long TIME_VALUE_SESSION_AUTO_LOGIN = 2880;
	
	public static final String SESSION = "Session_";
	public static final String MAIL_TOKEN = "Token_";	
	public static  final int  HTTP_STATUS_OK = 200;
	
	public static  final String MESSAGE_AUTH_FAIL = "INVALID_CREDENTIALS";
	public static  final String MESSAGE_AUTO_LOGIN_FAIL = "INVALID_CREDENTIALS";
	public static  final String MESSAGE_AUTH_BAD_REQUEST = "USER_NOT_FOUND";
	public static  final String MESSAGE_INTERNAL_ERROR = "INTERNAL_ERROR";
	public static  final String MESSAGE_EMAIL_EMPTY_ERROR = "EMAIL_EMPTY_ERROR";
	public static  final String USER_DISABLED = "USER_DISABLED";
	public static  final String USER_NO_ENABLED = "USER_NO_ENABLED";
	public static  final String MESSAGE_AUTH_BLOCK_REQUEST = "FAILED_ATTEMPTS_EXCEEDED";
	public static  final String  USER_CODE_ENABLED = "H";
	public static  final String  USER_CODE_DISABLED = "D";
	
	public  static  final int MAX_ATTEMPT = 3;
	public static final String HOST_NOTIFY = System.getenv("MS-NOTIFY");
	public static final String API_URL_ATTEMPT= "api/v1/notify/send-notify";
	public static final String TYPE_NOTIFY= "ATTEMPT_PASSWORD";
	public static final String CODE_ADMIN = "ROLE_1";
	public static final String HOST_BLOCKCHAIN = System.getenv("MS-BLOCKCHAIN");
	public static final String API_URL_BLOCKCHAIN_USER_CAP = "/api/v1/blockchain/user-cap";
	
}
