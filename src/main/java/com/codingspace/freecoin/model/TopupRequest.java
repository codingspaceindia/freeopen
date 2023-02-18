package com.codingspace.freecoin.model;


import java.util.Calendar;
import java.util.Date;
import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class TopupRequest {
    @Id
    String id;
	String userId;
    String name;
	String refId;
    double amount;
    Date requestedAt=Calendar.getInstance().getTime();
    String status="P";
    Date respondedAt;
    Boolean is365Days = false;
    Boolean is365Reduction = false;
    String proofImageUrl;     
}
