package com.invest.honduras.service;

import com.invest.honduras.domain.entity.User;

public interface LoginService {

	public String login(User loginrequest , long time);
	public String logout(String session);	
	public String refreshToken(String session,User loginrequest , long time);	
	public String autologin(String session);

}
