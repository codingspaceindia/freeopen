package com.codingspace.freecoin.model.reqres;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ChangePasswordReq {
    public String userId;
    public String oldPassword;
    public String newPassword;
}
