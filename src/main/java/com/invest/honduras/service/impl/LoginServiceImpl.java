package com.invest.honduras.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.Claim;
import com.invest.honduras.config.client.RedisClient;
import com.invest.honduras.domain.entity.User;
import com.invest.honduras.security.util.JWTUtil;
import com.invest.honduras.service.LoginService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Util;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	RedisClient redisClient;

	@Override
	public String login(User loginrequest,long time) {
		String uuid = Util.generateRandomUuid();
		String token = jwtUtil.generateToken(loginrequest, uuid,time);
		String keyValue = Constant.SESSION + loginrequest.getId();

		if (!redisClient.existSession(keyValue)) {
			redisClient.setValueSession(keyValue, uuid ,time);
		}

		return token;
	}

	@Override
	public String logout(String token) {

		Map<String, Claim> mapClaim = jwtUtil.getAllClaimsToken(token);

		String logout = "";
		String keyValue = Constant.SESSION + mapClaim.get("uuid").asString();
		if (redisClient.existSession(keyValue)) {
			redisClient.closeSession(keyValue);
			logout = "ok";
		}
		return logout;
	}

	@Override
	public String refreshToken(String token, User loginrequest, long time) {
		Map<String, Claim> mapClaim = jwtUtil.getAllClaimsToken(token);

		String keyValue = Constant.SESSION + mapClaim.get("uuid").asString();
		if (redisClient.existSession(keyValue)) {
			token = jwtUtil.generateToken(loginrequest, redisClient.getSession(keyValue), time);
			redisClient.updateSession(keyValue, redisClient.getSession(keyValue) ,time);
		}

		return token;
	}

	@Override
	public String autologin(String session) {
		// TODO Auto-generated method stub

		return null;
	}

}
