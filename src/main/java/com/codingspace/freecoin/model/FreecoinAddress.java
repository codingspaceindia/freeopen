package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Document(collection = "freeCoinAddress")
public class FreecoinAddress 
{
	@Id
    String id;
	
    String userId;
    String freeCoinAddress;
}
