package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "SupportRecord")
@Getter
@Setter
public class SupportRecord {
	
	@Id
	String SupportId;
    String refId;	
    String PhoneNumber;
    String email;
    String SupportMessage;
    String isProcessed ="false";
}


