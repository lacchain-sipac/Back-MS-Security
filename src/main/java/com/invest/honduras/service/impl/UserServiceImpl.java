package com.invest.honduras.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.everis.blockchain.algorithm.Secp256k1Utils;
import com.everis.blockchain.algorithm.model.JwtPayload;
import com.invest.honduras.blockchain.impl.BlockChainClient;
import com.invest.honduras.dao.RoleDao;
import com.invest.honduras.dao.UserDao;
import com.invest.honduras.domain.entity.User;
import com.invest.honduras.domain.entity.rest.AuthRequest;
import com.invest.honduras.domain.entity.rest.AuthRequest2FA;
import com.invest.honduras.domain.entity.rest.UserCapRequest;
import com.invest.honduras.enums.TypeStatusCode;
import com.invest.honduras.error.GeneralRuntimeException;
import com.invest.honduras.error.GlobalException;
import com.invest.honduras.security.util.PBKDF2Encoder;
import com.invest.honduras.service.LoginAttemptService;
import com.invest.honduras.service.RedisService;
import com.invest.honduras.service.UserService;
import com.invest.honduras.util.Constant;
import com.invest.honduras.util.Otp;
import com.invest.honduras.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	RedisService redisService;

	@Autowired
	BlockChainClient blockChainClient;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Override
	public Mono<User> findByUserName(AuthRequest authRequest) {
		Mono<User> monoUser = userDao.findByUserName(authRequest.getUsername().toLowerCase()).doOnSuccess(user -> {

			if (user == null) {

				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_AUTH_BAD_REQUEST);

			}
						
			if (Constant.USER_CODE_DISABLED.equals(user.getCodeStatus())) {
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
						TypeStatusCode.USER_DISABLED.getMessage());

			}
			if (!Constant.USER_CODE_ENABLED.equals(user.getCodeStatus())) {
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
						TypeStatusCode.USER_DISABLED.getMessage());
			}
			// String ip =requestUtil.getClientIP();
			log.info("password:" + passwordEncoder.encode(authRequest.getPassword()));
			if (!(passwordEncoder.encode(authRequest.getPassword()).equals(user.getPassword()))) {
				loginAttemptService.loginFailed(user.getId());
				// lanzamos el error a la clase generica
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.MESSAGE_AUTH_FAIL.getCode(),
						TypeStatusCode.MESSAGE_AUTH_FAIL.getMessage());
				// throw new GlobalException(HttpStatus.OK, Constant.MESSAGE_AUTH_FAIL);
			}

		});
		return monoUser;
	}
	
	@Override
	public Mono<User> verificationCode(String token, AuthRequest2FA authRequest) {
		
		String username = Util.decodeToken(token);

		if(username == null){				
			throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_AUTH_BAD_REQUEST);
		}
		
		Mono<User> monoUser = userDao.findByUserName(username.toLowerCase()).doOnSuccess(user -> {
			
			if(!(Otp.verificationCode2FA(authRequest.getVerifiedCode(), user.getSecretkey()))){				
				
				throw new GeneralRuntimeException(HttpStatus.OK, 
												  TypeStatusCode.MESSAGE_CODE_BAD_REQUEST.getCode(),
												  TypeStatusCode.MESSAGE_CODE_BAD_REQUEST.getMessage());
				
			}
		});
		
		return monoUser;
	}


	@Override
	public Mono<User> findByUserEmail(String email) {
		Mono<User> monoUser = userDao.findByUserName(email.toLowerCase()).doOnSuccess(user -> {

			if (user == null) {
				// throw new GlobalException(HttpStatus.NOT_FOUND,
				// Constant.MESSAGE_AUTH_BAD_REQUEST);
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_NOT_EXIST.getCode(),
						TypeStatusCode.USER_NOT_EXIST.getMessage());
			}

			if (Constant.USER_CODE_DISABLED.equals(user.getCodeStatus())) {
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
						TypeStatusCode.USER_DISABLED.getMessage());
			}

			if (!Constant.USER_CODE_ENABLED.equals(user.getCodeStatus())) {
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
						TypeStatusCode.USER_DISABLED.getMessage());
			}

		});
		return monoUser;
	}

	@Override
	public Mono<User> findByUserDid(JwtPayload jwtPayload) {

		return userDao.findByUserDid(jwtPayload.getSub()).flatMap(user -> {
			try {
				

				if (Constant.USER_CODE_DISABLED.equals(user.getCodeStatus())) {
					throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
							TypeStatusCode.USER_DISABLED.getMessage());
				}

				if (!Constant.USER_CODE_ENABLED.equals(user.getCodeStatus())) {
					throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DISABLED.getCode(),
							TypeStatusCode.USER_DISABLED.getMessage());
				}

				return checkCap(user.getProxyAddress(), Secp256k1Utils.publicKeyToAddress(jwtPayload.getIss()))
						.flatMap(flag -> {
							if (!flag) {
								throw new GeneralRuntimeException(HttpStatus.OK,
										TypeStatusCode.USER_DID_VALIDATE.getCode(),
										TypeStatusCode.USER_DID_VALIDATE.getMessage());
							}

							return Mono.just(user);

						});

			} catch (Exception e) {
				log.error("error.checkCap", e);
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DID_VALIDATE.getCode(),
						TypeStatusCode.USER_DID_VALIDATE.getMessage());
			}

		}).switchIfEmpty(
				 
				Mono.error( new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.USER_DID_NOT_FOUND.getCode(),
				TypeStatusCode.USER_DID_NOT_FOUND.getMessage()) ));
	}

	@Override
	public Mono<User> findByUserIdByToken(String token) {
		return userDao.findByUserIdByToken(token).doOnSuccess(user -> {

			if (user == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_AUTH_BAD_REQUEST);
			}

		});

	}

	@Override
	public Mono<User> findByUserId(String id) {
		redisService.existSession(Constant.MAIL_TOKEN.concat(id));

		Mono<User> monoUser = userDao.findByUserId(id).doOnSuccess(user -> {

			if (user == null) {
				throw new GlobalException(HttpStatus.NOT_FOUND, Constant.MESSAGE_AUTH_BAD_REQUEST);
			}

		});

		return monoUser;
	}

	private Mono<Boolean> checkCap(String proxyAddressUser, String device) {
		UserCapRequest request = new UserCapRequest();
		request.setProxyAddressUser(proxyAddressUser);
		request.setAddressDevice(device);
		return blockChainClient.checkCap(request).map(response -> {
			if (com.invest.honduras.enums.TypeStatusCode.OK.getCode().equals(response.getStatus())) {

				return "1".equals(response.getData().getResult());
			} else {
				throw new GeneralRuntimeException(HttpStatus.OK, TypeStatusCode.SOLICITUDE_ID_NOT_FOUND.getCode(),
						response.getMessage());
			}

		});
	}


}
