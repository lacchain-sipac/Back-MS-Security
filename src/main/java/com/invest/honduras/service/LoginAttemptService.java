package com.invest.honduras.service;


public interface LoginAttemptService {

	 void resetAttempt(String key);
	 void loginFailed(String key);	 
	 boolean coveredAttempt(String key);
	 
}
