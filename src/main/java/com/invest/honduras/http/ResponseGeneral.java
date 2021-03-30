package com.invest.honduras.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGeneral {

	 private String status;
	 private ResponseItem data;
	 
	 private String message;

}