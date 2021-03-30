package com.invest.honduras.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.invest.honduras.service.LoginAttemptService;
import com.invest.honduras.util.Constant;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
		   	   
	    private LoadingCache<String, Integer> attemptsCache;	 
	    
	    public LoginAttemptServiceImpl() {
	        super();
	        attemptsCache = CacheBuilder.newBuilder().
	          expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
	            public Integer load(String key) {
	                return 0;
	            }
	        });
	    }
	    
	     
	@Override
	public void resetAttempt(String key) {
		// TODO Auto-generated method stub
		
		 attemptsCache.invalidate(key);
		 
	}

	@Override
	public void loginFailed(String key) {
		
		// TODO Auto-generated method stub
	     int attempts = 0;
	        try {
	            attempts = attemptsCache.get(key);
	        } catch (ExecutionException e) {
	            attempts = 0;
	        }
	        attempts++;
	        attemptsCache.put(key, attempts);
	}

	@Override
	public boolean coveredAttempt(String key) {
		  try {
			  int intent = attemptsCache.get(key);			  
	            return intent >= Constant.MAX_ATTEMPT;	            
	        } catch (ExecutionException e) {
	            return false;
	        }
		  }

}
