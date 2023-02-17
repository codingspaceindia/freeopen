package com.codingspace.freecoin.repository;

import java.util.List;

import com.codingspace.freecoin.model.Auth;

public interface AuthRepository {
    /**
     * Create user data
     */
    public Auth createAuth(String userId, String password);

    /**
     * Validate userId and password. If matches returns true. Otherwise returns
     * false.
     */
    public Boolean validate(String userId, String password);

    /**
     * @note Need to validate
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Auth changePassword(String userId, String oldPassword, String newPassword);

    /**
     * Get all user's auth list
     * 
     * @return
     */
    public List<Auth> getAllAuthList();
    
    public Auth getAuthByUserId(String userId);
}
