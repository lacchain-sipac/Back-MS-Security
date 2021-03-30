package com.invest.honduras.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.invest.honduras.security.AuthenticationManager;
import com.invest.honduras.security.SecurityContextRepository;

import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SecurityContextRepository securityContextRepository;
	
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
                .exceptionHandling()                
                .authenticationEntryPoint((swe, e) -> {
                    return Mono.fromRunnable(() -> {
                    	swe.getResponse().setStatusCode( HttpStatus.UNAUTHORIZED);
                    });
                })
                .accessDeniedHandler((swe, e) -> {
                    return Mono.fromRunnable(() -> {
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    });
                })
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()                
                .pathMatchers( HttpMethod.OPTIONS, "/**").permitAll()
                .pathMatchers("/api/v1/auth/login/**").permitAll()
                .pathMatchers("/api/v1/auth/login2FA/**").permitAll()
                .pathMatchers("/api/v1/auth/login-jwt/**").permitAll()
                .pathMatchers("/api/v1/auth/auto-login/**").permitAll()
                .pathMatchers("/ms-user/api/v1/user/forget-password/**").permitAll()
                .pathMatchers("/swagger-resources/**").permitAll()
                .pathMatchers("/v2/api-docs/**").permitAll()
                .pathMatchers("/favicon.ico").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers( "/config/**" ).permitAll()
                .pathMatchers( "/**/swagger-ui.html/**" ).permitAll()
                .pathMatchers("/**/swagger-resources/**").permitAll()
                .pathMatchers("/**/v2/api-docs/**").permitAll()
                .pathMatchers("/**/favicon.ico").permitAll()
                .pathMatchers("/**/webjars/**").permitAll()
                .pathMatchers( "/**/config/**" ).permitAll()
                .pathMatchers("/ms-notify/**").permitAll()
                .pathMatchers("/ms-user/api/v1/user/did/register-did/**").permitAll()
                .pathMatchers("/ms-parameter/**").permitAll()               
                //.pathMatchers("/ms-socket/api/authentication/**").permitAll()
                .anyExchange().authenticated()
                .and().build();
    }
}
