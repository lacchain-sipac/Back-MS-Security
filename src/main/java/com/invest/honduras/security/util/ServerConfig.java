package com.invest.honduras.security.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@Data
public class ServerConfig {

	ServerHttpRequest request;
	ServerHttpResponse response;
}
