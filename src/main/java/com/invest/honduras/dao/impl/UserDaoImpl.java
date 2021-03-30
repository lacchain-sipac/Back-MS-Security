package com.invest.honduras.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.Claim;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.entity.User;
import com.invest.honduras.repository.UserRepository;
import com.invest.honduras.security.util.JWTUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class UserDaoImpl implements UserDao  {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Override
	public Mono<User> findByUserName(String userName) {
		return userRepository.findByEmail(userName);
		
	}
	
	@Override
	public Mono<User> findByUserEmail(String email) {
		return userRepository.findByEmail(email);
		
	}
	
	@Override
	public Mono<User> findByUserDid(String did) {
		return userRepository.findByDid(did);
	}

	@Override
	public Mono<User> findByUserIdByToken(String token) {
		
		Map<String, Claim> mapClaim = jwtUtil.getAllClaimsToken(token);			
		String userid = mapClaim.get("uuid").asString();
		return userRepository.findById(userid);
	}

	@Override
	public Mono<User> findByUserId(String id) {
		return userRepository.findById(id);
	}

	@Override
	public Flux<User> findUserByRol(String rol) {
		return userRepository.findUserByRol(rol);
	}

	
}

