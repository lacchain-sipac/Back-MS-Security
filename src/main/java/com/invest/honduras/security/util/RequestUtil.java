package com.invest.honduras.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class RequestUtil {

	@Autowired
	ServerConfig serverConfig;
	
	public String getClientIP() {
		
		ServerHttpRequest request = serverConfig.getRequest();
		String address = request.getRemoteAddress().getAddress().getHostAddress();
	    return address;
	    
	}
}
