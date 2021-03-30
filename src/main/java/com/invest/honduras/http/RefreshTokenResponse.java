package com.invest.honduras.http;

import com.invest.honduras.domain.entity.rest.AuthResponse;
public class RefreshTokenResponse  extends HttpResponse<AuthResponse> {
	
	public RefreshTokenResponse(String status, AuthResponse response ,String message) {
		super(status, response , message );
		 	// TODO Auto-generated constructor stub
	}

}
