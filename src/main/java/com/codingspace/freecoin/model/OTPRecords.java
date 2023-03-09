package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "OTPRecords")
@Getter
@Setter
public class OTPRecords {
	@Id
    String userId;
    String otp;
}

