package com.invest.honduras.config.client;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import com.invest.honduras.config.ApplicationProperties;
import com.invest.honduras.util.Constant;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient implements SessionClient {
    
	
    @Autowired
    private ApplicationProperties applicationProperties;

	private JedisConnectionFactory jedisFactory;
	private RedisTemplate<String, Object> redisTemplate;

	@SuppressWarnings("deprecation")
	@PostConstruct
	public void init() {
		jedisFactory = new JedisConnectionFactory();
		jedisFactory.setHostName(applicationProperties.getRedisHost());
		jedisFactory.setPort(applicationProperties.getRedisPort());
		jedisFactory.afterPropertiesSet();
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisFactory);
		redisTemplate.afterPropertiesSet();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

	}

	@Override
	public String getSession(String key) {
		Object jsonRedis = redisTemplate.opsForValue().get(key);
		return (String) jsonRedis;
	}

	@Override
	public boolean closeSession(String key) {
		boolean close = false;
		Object jsonRedis = redisTemplate.opsForValue().get(key);
		if (jsonRedis != null) {
			redisTemplate.delete(key);
			close = true;
		}
		return close;
	}

	@Override
	public boolean updateSession(String key, Object value , long time ) {
		
		boolean update = false ;
		if (existSession(key)) {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key,time, TimeUnit.MINUTES);
			update = true ;
		}
		return update;
	}

	@Override
	public String setValueSession(String key, Object value, long time) {
	
		
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key,time , TimeUnit.MINUTES);
		
		return "";
	}

	@Override
	public boolean existSession(String key) {
		Object jsonRedis = redisTemplate.opsForValue().get(key);
		if(jsonRedis != null) {
			return true ;
		}
		return false;
	}

}
