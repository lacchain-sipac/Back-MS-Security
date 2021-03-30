package com.invest.honduras.blockchain.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.invest.honduras.domain.entity.rest.UserCapRequest;
import com.invest.honduras.http.ResponseGeneral;
import com.invest.honduras.util.Constant;

import reactor.core.publisher.Mono;

@Component
public class BlockChainClient {

	private final WebClient webClient;

	public BlockChainClient() {
		this.webClient = WebClient.builder().baseUrl(Constant.HOST_BLOCKCHAIN)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public Mono<ResponseGeneral> checkCap(UserCapRequest request) {

		return this.webClient.post().uri(Constant.API_URL_BLOCKCHAIN_USER_CAP).accept(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(request), UserCapRequest.class).retrieve().bodyToMono(ResponseGeneral.class)
				.doOnError(e -> {
					System.out.println("doOnError: " + e.getMessage());
					throw new RuntimeException();
				}).map(data -> {
					System.out.println("enviando correo--->>>>" + data);
					return Mono.just(data);
				}).block();
	}

}
