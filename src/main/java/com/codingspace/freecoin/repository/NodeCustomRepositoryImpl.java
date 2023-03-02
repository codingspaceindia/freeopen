package com.codingspace.freecoin.repository;

import java.util.List;

import com.codingspace.freecoin.model.Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class NodeCustomRepositoryImpl implements NodeCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Node getByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Node.class);
    }

    @Override
    public Node updateNodeLeft(Node parentNode, Node childNode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(parentNode.getUserId()));
        Update update = new Update();
        update.set("left", childNode);
        return mongoTemplate.findAndModify(query, update, Node.class);
    }

    @Override
    public Node updateNodeRight(Node parentNode, Node childNode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(parentNode.getUserId()));
        Update update = new Update();
        update.set("right", childNode);
        return mongoTemplate.findAndModify(query, update, Node.class);
    }

    @Override
    public List<Node> getAllNodes() {
        return mongoTemplate.findAll(Node.class);
    }

    @Override
    public List<Node> getPlacementParentUserId(String userId){
        Query query = new Query();
        query.addCriteria(Criteria.where("left.userId").is(userId));
        query.addCriteria(Criteria.where("right.userId").is(userId));
        return mongoTemplate.find(query, Node.class);
    }

}
