package com.codingspace.freecoin.model;

public enum TransactionMessage {
    /** Joining bonus */
    NEW_JOIN,
    /** New child joined in right side */
    NEW_RIGHT_CHILD_JOIN,
    /** New child joined in left side */
    NEW_LEFT_CHILD_JOIN,
    /** Topup done */
    TOPUP_DONE,
    /** (10%) Returns from child topup */
    CHILD_TOPUP_DONE,
    /** 10% Returns to user due to self-topup */
    TOPUP_SELF_RETURN,
    
    /** Stacking/Topup done previously (Data inserted from excel) */
    PREVIOUS_TOPUP_STACKING,
    /** Previous earning balance (Data inserted from excel) */
    PREVIOUS_EARNING_BALANCE,
    /** Previous available balance (Data inserted from excel) */
    PREVIOUS_AVAILABLE_BALANCE,
    /** User Withdraw Message */
    USER_WITHDRAW,
    /**Daily Topup ROI */
    INTERNAL_TRANSFER,

    TRANSFER_WITHDRAW,
    ROI,
    //Pair Matching
    BUSINESS_MATCH
    
}
