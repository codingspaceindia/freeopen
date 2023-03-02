package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "UserStatus")
public class UserStatus {
	@Id
	private String userId;
	
    private String name;
    private String refId;
    private Boolean active = false;
}
