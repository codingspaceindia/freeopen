package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "auth")
@Getter
@Setter
public class Auth {

    @Id
    private String id;
    
    private String email;
    private String refId;
    private String name;
    private String userId;
    private String password;
}
