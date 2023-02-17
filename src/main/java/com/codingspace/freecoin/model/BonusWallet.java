package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Document(collection = "bonusWallet")
public class BonusWallet 
{
	@Id
    String id;
	
    String userId;
    double amount;
    
	public BonusWallet(String userId, double amount) 
	{
		this.userId = userId;
		this.amount = amount;
	}
}
