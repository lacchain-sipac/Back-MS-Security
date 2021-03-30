package com.invest.honduras.domain.entity.rest;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Data;

@Data
public class AuthRequest2FA {
	
	@NotNull
	private String verifiedCode;
	
}
