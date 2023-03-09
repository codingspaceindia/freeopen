package com.codingspace.freecoin.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferWithdrawRequest {
    private String fromRefId;
    private String toRefId;
    private double amount;
}
