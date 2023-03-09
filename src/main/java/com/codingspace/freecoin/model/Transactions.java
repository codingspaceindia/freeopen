package com.codingspace.freecoin.model;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Transactions {
    private TransactionType type; // C => Credit, D => Debit
    private Date transactionAt;
    private double amount;
    private String message;
    private boolean isProcessed = false;
    private double fcAmount;
    private Date topupDoneDate;
    

    public Transactions(double amount, TransactionType type, String message) {
        this.amount = amount;
        this.type = type;
        this.transactionAt = new Date();
        this.message = message;
    }


	public Transactions(double amount,TransactionType type, String message, double fcAmount) {
		this.type = type;
		this.amount = amount;
		this.message = message;
		this.fcAmount = fcAmount;
		this.transactionAt = new Date();
	}


	public Transactions() {

	}
    
}
