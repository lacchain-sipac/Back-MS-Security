package com.invest.honduras.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invest.honduras.dao.RoleDao;
import com.invest.honduras.domain.entity.Role;
import com.invest.honduras.repository.RoleRepository;

import reactor.core.publisher.Flux;

@Component
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	RoleRepository roleRepository;
	@Override
	public Flux<Role> listRole() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

}
