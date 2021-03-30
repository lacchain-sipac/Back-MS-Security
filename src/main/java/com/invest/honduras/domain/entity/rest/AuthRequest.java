package com.invest.honduras.domain.entity.rest;


import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AuthRequest {

	@NotNull
	private String username;
	@NotNull
	private String password;
}
