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
@Document(collection="pairMatchingRecord")
public class PairMatchingRecord 
{
	@Id
	private String userId;
	
	private Date pairMatchedDate = Calendar.getInstance().getTime();
	private double pairMatchedAmount; 
}

