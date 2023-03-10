package com.codingspace.freecoin.repository;

import com.codingspace.freecoin.model.Wallet;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Wallet, String>, WalletCustomRepository {

}