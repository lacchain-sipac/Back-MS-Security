package com.invest.honduras.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc){
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe){
		
		ServerHttpRequest request = swe.getRequest();
		log.info("Entro load: " + request.getURI());
		log.info("load: method " + request.getMethod());				
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);		
		log.info("load: getHeaders " + request.getHeaders());
		if(authHeader != null ) {
			Authentication auth = new UsernamePasswordAuthenticationToken(authHeader, authHeader);
			return this.authenticationManager.authenticate(auth).map((authentication)-> {
				return new SecurityContextImpl(authentication);
			});
		} else {
			return Mono.empty();
		}
		
	}


}
