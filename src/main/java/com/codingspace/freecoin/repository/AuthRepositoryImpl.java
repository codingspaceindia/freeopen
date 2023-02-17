package com.codingspace.freecoin.repository;

import java.util.List;

import com.codingspace.freecoin.model.Auth;
import com.codingspace.freecoin.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserService userService;

    @Override
    public Auth createAuth(String userId, String password) {
        Auth newAuth = new Auth();
        newAuth.setUserId(userId);
        // newAuth.setPassword(userService.generateRandomString(8));
        newAuth.setPassword(password);
        Auth savedAuth = mongoTemplate.save(newAuth);
        return savedAuth;
    }

    @Override
    public Boolean validate(String userId, String password) {

        Auth userAuth = this.getUserAuthData(userId);

        if (userAuth == null) {
            return false;
        }
        return password.equals(userAuth.getPassword());
    }

    private Auth getUserAuthData(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Auth.class);
    }

    @Override
    public Auth changePassword(String userId, String oldPassword, String newPassword) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        Update update = new Update();
        update.set("password", newPassword);
        return mongoTemplate.findAndModify(query, update, Auth.class);
    }

    @Override
    public List<Auth> getAllAuthList() {
        return mongoTemplate.findAll(Auth.class);
    }
    
    @Override
    public Auth getAuthByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Auth.class);
    }
}
