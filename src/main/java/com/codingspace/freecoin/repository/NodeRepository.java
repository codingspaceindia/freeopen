package com.codingspace.freecoin.repository;

import com.codingspace.freecoin.model.Node;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NodeRepository extends MongoRepository<Node, String>, NodeCustomRepository {

}
