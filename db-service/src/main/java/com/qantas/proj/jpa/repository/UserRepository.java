package com.qantas.proj.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.qantas.proj.jpa.entities.User;

@RepositoryRestResource(collectionResourceRel="user",path="user")
public interface UserRepository extends JpaRepository<User, String>{
}
