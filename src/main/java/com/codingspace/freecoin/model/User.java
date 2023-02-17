package com.codingspace.freecoin.model;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "user")
public class User{

    @Id
    private String id;
    private String name;
    private String gender; // "M","F"
    private String mailId;
    private String address;
    private String status;
    private String refId;
    private String parentId;
    private String NameOfParent;
    private String city;
    private String country;
    private String parentRefId;
    private String mobileNumber;
    private Date joiningDate;
    private Date DOB;
    private String nominee;
    private String PIN;
    private String relation;
    
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}


}
