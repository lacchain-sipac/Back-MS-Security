package com.invest.honduras.util;

import org.jboss.aerogear.security.otp.Totp;

public class Otp {
			
	public static boolean verificationCode2FA(String code, String secretKey) {
		boolean result = true;
		
        Totp totp = new Totp(secretKey);
        if  (!totp.verify(code)) {
        	result = false;
        }	
        return result;
	}
}
