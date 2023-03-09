package com.codingspace.freecoin.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Document(collection = "TransferWithdrawTransactions")
@Component

public class TransferWithdrawTransactions {
    @Id
    private String id;

}
