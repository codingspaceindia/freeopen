package com.codingspace.freecoin.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Document(collection = "wallet")
public class Wallet {

    @Id
    private String id;

    private String userId;
    private double amount; // total amount
    private double offerWallet;
    private double earningsWallet;
    private double stackingWallet;
    private double totalStacking;
    private double roiAmount;
    private List<Transactions> transactions = new ArrayList<Transactions>(); // change

    public Wallet(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }
}
