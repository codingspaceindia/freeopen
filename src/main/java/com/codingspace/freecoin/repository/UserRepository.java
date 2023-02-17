package com.codingspace.freecoin.repository;

import com.codingspace.freecoin.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>, UserCustomRepository {

}