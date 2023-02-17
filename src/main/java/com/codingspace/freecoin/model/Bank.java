package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Document(collection = "bank")
public class Bank {
    @Id
    private String id;
    private String userId;
    private String accountNumber;
    private String ifscCode;
    private String branch;
    private String bankName;
}
