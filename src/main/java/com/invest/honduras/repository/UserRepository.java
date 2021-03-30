package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

	Mono<User> findByEmail(String email);
	
	Mono<User> findByDid(String did);

	@Query(value = "{'roles.code': ?0 }")
	Flux<User> findUserByRol(final String code);

}
