package com.codingspace.freecoin.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Document(collection = "TransferWalletTransactions")
@Component
public class TransferWalletTransactions {
    @Id
    private String id;
    private String name;
    private String fromRefId;
    private String toRefId;
    private String fromUserId;
    private String toUserId;
    private double amount;
    private Date respondedAt;
}
