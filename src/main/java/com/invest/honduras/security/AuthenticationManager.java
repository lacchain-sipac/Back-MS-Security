package com.invest.honduras.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.Claim;
import com.invest.honduras.config.client.RedisClient;
import com.invest.honduras.domain.entity.Role;
import com.invest.honduras.security.util.JWTUtil;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager  {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private RedisClient redisClient;
	
	@Override
	public Mono <Authentication> authenticate(Authentication authentication){
		String authToken = authentication.getCredentials().toString();
		
		log.info(" Init [AuthenticationManager][authenticate] :" + authToken);
		String username;
		Map<String, Claim> mapClaim = new HashMap<String, Claim>();
		boolean redis = false;
		try {
			username = jwtUtil.getUsernameFromToken(authToken);
			log.info("[AuthenticationManager] username:" + username);
			mapClaim  = jwtUtil.getAllClaimsToken(authToken);
			Claim uuidClaim = mapClaim.get("uuid");
			String uuid =  uuidClaim.asString();
			
			redis	=redisClient.existSession(Constant.SESSION+uuid);			
			log.info("[AuthenticationManager] validate uuid:" +uuid +"  and response redis: "+ redis );

		}catch (Exception e) {
			return Mono.empty();
		}
				
		if(username != null && jwtUtil.validateToken(authToken) && redis) {
			
			     			
			List<String> rolesMap = new ArrayList<String>();
			   Claim roleClaim = mapClaim.get("rol");
			rolesMap= roleClaim.asList(String.class);
			  List<Role> roles = new ArrayList<>();
				for (String rolemap : rolesMap) {
					roles.add(new Role(rolemap));
				}
				log.info("[AuthenticationManager] asociate roles:" + roles);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
								username,
								null,
								roles.stream().map(authority -> new SimpleGrantedAuthority(authority.getCode())).collect(Collectors.toList())
							);
			
			log.info("[AuthenticationManager] validate is correct :" + username); 
			return Mono.just(auth);
		}else {
			log.info("[AuthenticationManager] validate is not correct :" + username);
			return Mono.empty();
		}
	}
}
