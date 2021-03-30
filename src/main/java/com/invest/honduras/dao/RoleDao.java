package com.invest.honduras.dao;



import com.invest.honduras.domain.entity.Role;

import reactor.core.publisher.Flux;

public interface RoleDao {

	Flux<Role> listRole();
}
