package com.codingspace.freecoin.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Document(collection = "withdrawRequest")
public class WithdrawRequest {
	@Id
    private String id;
	
	private String userId;
	private String name;
	private String refId;
	private String walletType;
	private String currencyType;
	private double offerWalletDeduction;
    private double earningsWalletDeduction;
    private double stackingWalletDeduction;
	private double amount;
	private String status="P";
	private Date requestedAt=Calendar.getInstance().getTime();
	private Date respondedAt;
	private String action;
	private String senderId;
}

