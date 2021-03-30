package com.invest.honduras.service;

import com.everis.blockchain.algorithm.model.JwtPayload;
import com.invest.honduras.domain.entity.User;
import com.invest.honduras.domain.entity.rest.AuthRequest;
import com.invest.honduras.domain.entity.rest.AuthRequest2FA;

import reactor.core.publisher.Mono;

public interface UserService {

	Mono<User> findByUserName(AuthRequest authRequest);
	Mono<User> findByUserEmail(String email);
	Mono<User> findByUserIdByToken(String token);
	Mono<User> findByUserId(String id); 
	Mono<User> findByUserDid(JwtPayload jwtPayload);
	Mono<User> verificationCode(String token, AuthRequest2FA authRequest2FA);
	
}
