package com.ems.usercenter.model.request;

import lombok.Data;

@Data
public class UserUpdatePasswordReq {
    private String oldPass;
    private String newPass;
    private String confirm;
}
