package com.codingspace.freecoin.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Document(collection = "TopupTransactionsRecord")
public class TopupTransactionsRecord {
    @Id
    private String userId;
    private List<TopupRequest> topup_Transactions= new ArrayList<TopupRequest>();
    private Double totalTopUpStackingAmount;
}
