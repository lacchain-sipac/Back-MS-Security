package com.invest.honduras.http;

import com.invest.honduras.domain.entity.rest.AuthResponse;

public class LoginResponse  extends HttpResponse<AuthResponse> {
	
	public LoginResponse(String status, AuthResponse response , String  message) {
		super(status, response , message);
	}

}
