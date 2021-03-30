package com.invest.honduras.domain.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "role")
public class Role {
	
	private String code;
	
	
}
