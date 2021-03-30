package com.invest.honduras.config.client;

public interface SessionClient {
 
    boolean updateSession(String key, Object value, long time);
    
    String getSession(String key);
    
    String setValueSession(String key , Object value , long time);
    
    boolean closeSession(String key);
    
    boolean existSession(String key);
    
    
}
