package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Document(collection="businessRecord")
public class BusinessRecord {
	@Id
	private String userId;
	
	private double leftBusinessAmount;
	private double rightBusinessAmount; 
	private int leftUsers;
	private int leftActiveUsers;
	private int rightUsers;
	private int rightActiveUsers;
	private double totalStacking;
}


