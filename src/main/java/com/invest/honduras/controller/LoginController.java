package com.invest.honduras.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everis.blockchain.algorithm.Secp256k1Utils;
import com.everis.blockchain.algorithm.exception.JwtExpirationException;
import com.everis.blockchain.algorithm.exception.JwtVerifyException;
import com.everis.blockchain.algorithm.model.JwtPayload;
import com.invest.honduras.domain.entity.rest.AuthJwtRequest;
import com.invest.honduras.domain.entity.rest.AuthRequest;
import com.invest.honduras.domain.entity.rest.AuthRequest2FA;
import com.invest.honduras.domain.entity.rest.AuthResponse;
import com.invest.honduras.domain.entity.rest.AutoLoginRequest;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.http.LoginResponse;
import com.invest.honduras.http.LogoutResponse;
import com.invest.honduras.http.RefreshTokenResponse;
import com.invest.honduras.service.LoginService;
import com.invest.honduras.service.RedisService;
import com.invest.honduras.service.UserService;
import com.invest.honduras.util.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")

@Slf4j
public class LoginController {

	@Autowired
	LoginService loginService;

	@Autowired
	UserService userService;

	@Autowired
	RedisService redisService;

	@PostMapping(value = "/login", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<LoginResponse>> login(@RequestBody @Valid AuthRequest loginrequest) {

		try {			
			
			return userService.findByUserName(loginrequest).map((userDetails) -> {

				return ResponseEntity.ok(new LoginResponse(TypeStatusCode.OK.getCode(),
						new AuthResponse(loginService.login(userDetails , Constant.TIME_VALUE_TOKEN ) ), TypeStatusCode.OK.getMessage()));

			}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

		} catch (Exception e) {
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_INTERNAL_ERROR);
		}
	}

	@PostMapping(value = "/login2FA", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<LoginResponse>> login2FA(@RequestHeader("Authorization") String token,
														@RequestBody @Valid AuthRequest2FA loginrequest) {
		
		try {			
			
			return userService.verificationCode(token, loginrequest).map((userDetails) -> {

				return ResponseEntity.ok(new LoginResponse(TypeStatusCode.OK.getCode(),
										 					null, 
										 					TypeStatusCode.OK.getMessage()));

			}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

		} catch (Exception e) {
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_INTERNAL_ERROR);
		}
	}

	
	
	@PostMapping(value = "/login-jwt", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<LoginResponse>> loginQr(@RequestBody AuthJwtRequest auth) {

		log.info("loginQr.auth =" + auth);
		JwtPayload jwtPayload = null; 
		try {
			
			  jwtPayload = Secp256k1Utils.verifyJWTokenSecp256k1(auth.getToken(),null);
			 
			
		} catch (JwtExpirationException | JwtVerifyException e) {
			log.error("Error.loginQr", e);
		 
				 return Mono.just(ResponseEntity
					.ok(new LoginResponse(TypeStatusCode.USER_NOT_AUTHORIZED.getCode(), null,  e.getMessage())));
		 
		} catch (Exception e) {
			log.error("Error.loginQr", e);
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_INTERNAL_ERROR);

		}
		
		if(Strings.isEmpty(jwtPayload.getSub()) ) {
			 return Mono.just(ResponseEntity
						.ok(new LoginResponse(TypeStatusCode.USER_DID_EMPTY.getCode(), null,  TypeStatusCode.USER_DID_EMPTY.getMessage())));
		}
		
		try {

			return userService.findByUserDid(jwtPayload).map((userDetails) -> {

				return ResponseEntity.ok(new LoginResponse(TypeStatusCode.OK.getCode(),
						new AuthResponse(loginService.login(userDetails,Constant.TIME_VALUE_SESSION )), TypeStatusCode.OK.getMessage()));
 	
			}).defaultIfEmpty(ResponseEntity.notFound().build());

		} catch (Exception e) {
			log.error("Error.loginQr", e);
			
			throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, Constant.MESSAGE_INTERNAL_ERROR);

		}
	}

	@PostMapping(value = "/logout", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String token) {
		String messsage = "Logout  Not Succesfully";
		String logout = loginService.logout(token);
		if (!logout.isEmpty()) {
			messsage = "Logout Succesfully";
		}
		return ResponseEntity
				.ok(new LogoutResponse(TypeStatusCode.OK.getCode(), messsage, TypeStatusCode.OK.getMessage()));
	}

	@PostMapping(value = "/refresh-token", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public Mono<ResponseEntity<RefreshTokenResponse>> refreshToken(@RequestHeader("Authorization") String token) {

		return userService.findByUserIdByToken(token).map((userDetails) -> {
			return ResponseEntity.ok(new RefreshTokenResponse(TypeStatusCode.OK.getCode(),
					new AuthResponse(loginService.refreshToken(token, userDetails , Constant.TIME_VALUE_SESSION)), TypeStatusCode.OK.getMessage()));
		}).defaultIfEmpty(ResponseEntity.notFound().build());

	}

	@PostMapping(value = "/auto-login", produces = { MediaType.APPLICATION_STREAM_JSON_VALUE,
			MediaType.APPLICATION_JSON_VALUE })

	public Mono<ResponseEntity<LoginResponse>> autologin(@RequestBody AutoLoginRequest autoLogin) {
		return userService.findByUserId(autoLogin.getAccess_token()).map((userDetails) -> {			
			return ResponseEntity.ok(new LoginResponse(TypeStatusCode.OK.getCode(),
					new AuthResponse(loginService.login(userDetails , Constant.TIME_VALUE_SESSION_AUTO_LOGIN)), TypeStatusCode.OK.getMessage()));

		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

	}

}
