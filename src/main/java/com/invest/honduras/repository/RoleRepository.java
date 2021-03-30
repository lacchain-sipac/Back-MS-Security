package com.invest.honduras.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.invest.honduras.domain.entity.Role;

public interface RoleRepository extends ReactiveMongoRepository<Role, String> {

}
