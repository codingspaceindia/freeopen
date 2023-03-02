package com.codingspace.freecoin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "PanRecord")
@Getter
@Setter
public class PanRecord 
{
	@Id
    String userId;
	
    String NameInPan;
    String PanNumber;
    String PanImageUrl;
}



