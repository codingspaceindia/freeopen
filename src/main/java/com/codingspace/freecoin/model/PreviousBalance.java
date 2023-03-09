package com.codingspace.freecoin.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "previousBalance")
public class PreviousBalance {
    @Id
    private String id;
    private String userId;
    private String refId;
    private String name;
    private double availableBalance;
    private double earningBalance;
    private double stacking;
    private Date stackingAt;

}
