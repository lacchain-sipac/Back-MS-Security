package com.invest.honduras.service;

public interface RedisService {

    boolean updateSession(String key, Object value ,  long time);
    
    String getSession(String key);
    boolean closeSession(String key);
    boolean existSession(String key);
}
