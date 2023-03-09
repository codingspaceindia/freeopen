package com.codingspace.freecoin.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.codingspace.freecoin.model.Bank;
import com.codingspace.freecoin.model.Dailyprice;
import com.codingspace.freecoin.model.Notice;
import com.codingspace.freecoin.model.PairMatchingRecord;
import com.codingspace.freecoin.model.PreviousBalance;
import com.codingspace.freecoin.model.Transactions;
import com.codingspace.freecoin.model.User;
import com.codingspace.freecoin.model.Fmdc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserCustomRepositoryImpl implements UserCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public User getByRefId(String refId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("refId").is(refId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public List<User> getUsersWithRefIdOnly() {
        Query query = new Query();
        query.fields().include("refId");
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public User getById(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public List<User> getUserByIds(List<String> userIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(userIds));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public Notice createNotice(Notice newNotice) {
        return mongoTemplate.save(newNotice);
    }

    @Override
    public List<Notice> getNoticeByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Notice.class);
    }

    @Override
    public Bank createBankDetails(String userId) {
        if (userId != null) {
            Bank newBank = new Bank();
            newBank.setUserId(userId);
            return mongoTemplate.save(newBank);
        }
        return null;
    }

    @Override
    public Bank getBankDetailsByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, Bank.class);
    }

    @Override
    public Bank updateBankDetails(Bank bank) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(bank.getUserId()));
        Update update = new Update();

        if (bank.getAccountNumber() != null) {
            update.set("accountNumber", bank.getAccountNumber());
        }
        if (bank.getIfscCode() != null) {
            update.set("ifscCode", bank.getIfscCode());
        }
        if (bank.getBranch() != null) {
            update.set("branch", bank.getBranch());
        }
        if (bank.getBankName() != null) {
            update.set("city", bank.getBankName());
        }
        if (bank.getBankName() != null) {
            update.set("bankName", bank.getBankName());
        }
        return mongoTemplate.findAndModify(query, update, Bank.class);
    }

    @Override
    public User updateUserDetails(User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("Id").is(user.getId()));
        Update update = new Update();
        if (user.getName() != null) {
            update.set("name", user.getName());
        }
        if (user.getAddress() != null) {
            update.set("address", user.getAddress());
        }
        if (user.getMailId() != null) {
            update.set("mailId", user.getMailId());
        }
        if (user.getGender() != null) {
            update.set("gender", user.getGender());
        }
        if (user.getMobileNumber() != null) {
            update.set("mobileNumber", user.getMobileNumber());
            System.out.println(user.getMobileNumber());
        }
        if (user.getCity() != null) {
            update.set("city", user.getCity());
        }
        if (user.getCountry() != null) {
            update.set("country", user.getCountry());
        }
        if (user.getDOB() != null) {
            update.set("DOB", user.getDOB());
        }
        if (user.getNominee() != null) {
            update.set("nominee", user.getNominee());
        }
        if (user.getPIN() != null) {
            update.set("PIN", user.getPIN());
        }
        if (user.getRelation() != null) {
            update.set("relation", user.getRelation());
        }
        return mongoTemplate.findAndModify(query, update, User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Boolean createDailyPrice(Dailyprice dailyPriceReq) {
        return mongoTemplate.save(dailyPriceReq) != null;

    }

    @Override
    public Boolean changeDailyPrice(Dailyprice dailyPriceReq) {
        Query query = new Query();
        Update update = new Update();
        update.set("cost", dailyPriceReq.getCost());
        return mongoTemplate.findAndModify(query, update, Dailyprice.class) != null;
    }

    @Override
    public Dailyprice getDailyPrice() {
        Query query = new Query();
        return mongoTemplate.findOne(query, Dailyprice.class);
    }

    @Override
    public User getUserByEmailId(String emailId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("mailId").is(emailId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public User getUserByMobile(String mobileNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("mobileNumber").is(mobileNumber));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public Collection<PreviousBalance> setUserBalance(List<PreviousBalance> data) {
        return mongoTemplate.insertAll(data);
    }

    @Override
    public PreviousBalance setPreviousBalanceByUserId(PreviousBalance data) {
        return mongoTemplate.save(data);
    }

    @Override
    public PreviousBalance getUserPreviousBalanceByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.findOne(query, PreviousBalance.class);
    }

    @Override
    public User updateUserJoiningDate(String userId, Date joiningDate) {
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("id").is(userId));
        update.set("joiningDate", joiningDate);
        return mongoTemplate.findAndModify(query, update, User.class);
    }
    
    @Override
    public User createUser(User user)
    {
    	return mongoTemplate.save(user);
    }

    @Override
    public Fmdc createFmdc(Fmdc fmdc){
        return mongoTemplate.save(fmdc);
    }

    @Override
    public List<Fmdc> getAllFmdc(){
        return mongoTemplate.findAll(Fmdc.class);
    }
}
