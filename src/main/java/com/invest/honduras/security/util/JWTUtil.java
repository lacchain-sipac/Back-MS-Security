package com.invest.honduras.security.util;

import java.io.IOException;
import java.io.Serializable;
import java.security.interfaces.RSAKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.invest.honduras.domain.entity.Role;
import com.invest.honduras.domain.entity.User;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String PUBLIC_KEY_FILE_RSA = "/security/rsa-public.pem";
	private static final String PRIVATE_KEY_FILE_RSA = "/security/rsa-private.pem";

	@SuppressWarnings("deprecation")
	public Boolean validateToken(String token) {
		RSAKey key;
		if (token != null) {
			try {
				key = (RSAKey) PemUtils.readPublicKeyFromFile(PUBLIC_KEY_FILE_RSA, "RSA");
				JWT.require(Algorithm.RSA256(key))

						.build().verify(token);
				return true;
			} catch (JWTDecodeException e) {
				log.error("JWTDecodeException validateToken :" + e.getMessage() );
				

			} catch (IOException e) {
				
				log.error("IOException validateToken :" + e.getMessage());
			}
		}
		
		return false;

	}

	@SuppressWarnings("deprecation")
	public Map<String, Claim> getAllClaimsToken(String token) {
		RSAKey key;
		Map<String, Claim> claims = new HashMap<String, Claim>();
		if (token != null) {

			try {
				key = (RSAKey) PemUtils.readPublicKeyFromFile(PUBLIC_KEY_FILE_RSA, "RSA");
				DecodedJWT jwt = JWT.require(Algorithm.RSA256(key))
						// .withSubject(subject)
						.build().verify(token);
				claims = jwt.getClaims();
				// claims.
				return claims;
			} catch (JWTDecodeException e) {
				log.error("JWTDecodeException getAllClaimsToken :" + e.getMessage() );
				
			} catch (IOException e) {
				log.error("IOException getAllClaimsToken :" + e.getMessage() );
				
			}
		}
		return claims;
	}

	@SuppressWarnings("deprecation")
	public String getUsernameFromToken(String token) {
		RSAKey key;
		if (token != null) {
			String user = null;
			try {
				key = (RSAKey) PemUtils.readPublicKeyFromFile(PUBLIC_KEY_FILE_RSA, "RSA");
				DecodedJWT jwt = JWT.require(Algorithm.RSA256(key))
						// .withSubject(subject)
						.build().verify(token);
				user = jwt.getSubject();
				return user;
			} catch (JWTDecodeException e) {
				log.error("JWTDecodeException getUsernameFromToken:" + e.getMessage() );
				
				
				
			} catch (IOException e) {
				log.error("IOException getUsernameFromToken:" + e.getMessage() );
				
			}
		}
		return "";
	}

	public String generateToken(User user, String uuid , long time) {
		log.info("User generateToken" + user.getAuthorities());

		List<Role> userRole = user.getRoles();
		String[] claimRoles = new String[user.getRoles().size()];
		for (Role role : userRole) {
			claimRoles[userRole.indexOf(role)] = role.getCode();
		}
		return doGenerateToken(claimRoles, user.getUsername(), user.getId(), user.getSecretkey(),time );
	}

	@SuppressWarnings("deprecation")
	private String doGenerateToken(String[] claims, String username, String uuid, String secretkey, long time) {

		long expirationTimeLong = TimeoutUtils.toMillis(time, TimeUnit.MINUTES);
		RSAKey key;
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);
		try {
			key = (RSAKey) PemUtils.readPrivateKeyFromFile(PRIVATE_KEY_FILE_RSA, "RSA");
			Algorithm algorithm = Algorithm.RSA256(key);
			return JWT.create().withIssuer("invest").withIssuedAt(createdDate).withExpiresAt(expirationDate)
					.withSubject(username)
					.withClaim("uuid", uuid)
					.withClaim("secretkey", secretkey)
					.withArrayClaim("rol", claims)
					.sign(algorithm);

		} catch (IOException e) {
			
			log.error("IOException doGenerateToken:" + e.getMessage() );
			
			return null;
		}

	}
}
