package com.invest.honduras.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invest.honduras.config.client.RedisClient;
import com.invest.honduras.service.RedisService;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	RedisClient redisClient;

	@Override
	public boolean updateSession(String key, Object value , long time) {
		return redisClient.updateSession(key, value , time);
	}

	@Override
	public String getSession(String key) {
		return redisClient.getSession(key);
	}

	@Override
	public boolean closeSession(String key) {
		return redisClient.closeSession(key);
	}

	@Override
	public boolean existSession(String key) {
		return redisClient.existSession(key);
	}

}
