package com.codingspace.freecoin.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.codingspace.freecoin.model.Bank;
import com.codingspace.freecoin.model.Dailyprice;
import com.codingspace.freecoin.model.Notice;
import com.codingspace.freecoin.model.PreviousBalance;
import com.codingspace.freecoin.model.User;
import com.codingspace.freecoin.model.Fmdc;

public interface UserCustomRepository {

    /**
     * Get user by refId
     * 
     * @param refId
     * @return
     */
    public User getByRefId(String refId);

    public List<User> getUsersWithRefIdOnly();

    /**
     * Get user data by id
     * 
     * @param id
     * @return
     */
    public User getById(String id);

    /**
     * Get user data by array of userIds
     * 
     * @param userIds
     * @return
     */
    public List<User> getUserByIds(List<String> userIds);

    /**
     * Get all users in db
     * 
     * @return
     */
    public List<User> getAllUsers();

    /**
     * Create new notice
     * 
     * @param newNotice
     * @return
     */
    public Notice createNotice(Notice newNotice);

    /**
     * Get notice created by userId
     */
    public List<Notice> getNoticeByUserId(String userId);

    /**
     * Create bank data for user, while saving new user
     * 
     * @param userId
     * @return
     */
    public Bank createBankDetails(String userId);

    /**
     * Get bank data by userId
     * 
     * @param userId
     * @return
     */
    public Bank getBankDetailsByUserId(String userId);

    /**
     * Update bank details
     * 
     * @param bank
     * @return
     */
    public Bank updateBankDetails(Bank bank);

    public User updateUserDetails(User user);

    public Boolean createDailyPrice(Dailyprice dailyPriceReq);

    public Boolean changeDailyPrice(Dailyprice dailyPriceReq);

    public Dailyprice getDailyPrice();

    public User getUserByEmailId(String emailId);

    public User getUserByMobile(String mobileNumber);

    public Collection<PreviousBalance> setUserBalance(List<PreviousBalance> data);

    public PreviousBalance getUserPreviousBalanceByUserId(String userId);

    public User updateUserJoiningDate(String userId, Date userJoinDate);

    public PreviousBalance setPreviousBalanceByUserId(PreviousBalance data);

	public User createUser(User user);

    public Fmdc createFmdc (Fmdc fmdc);

    public List<Fmdc> getAllFmdc();

}
