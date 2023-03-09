package com.codingspace.freecoin.model.reqres;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SaveUserReq {
    private String parentRefId;
    private String name;
    private String gender;
    private String address;
    private String mailId;
    private String city;
    private String country;
    private Boolean isLeft;
    private Boolean isRight;
    private String mobileNumber;
    private String password;
    private String userRefId;
    private Date DOB;
    private String nominee;
    private String PIN;
    private String relation;
    private double stackingAmount;
    private double availableBalance;
}
