package com.invest.honduras.dao;

import com.invest.honduras.domain.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserDao {
	
	Mono<User> findByUserName(String userName);
	Mono<User> findByUserEmail(String email);
	Mono<User> findByUserIdByToken(String token);
	Mono<User> findByUserId(String id);
	Flux<User> findUserByRol(String rol);
	Mono<User> findByUserDid(String did) ;
}
