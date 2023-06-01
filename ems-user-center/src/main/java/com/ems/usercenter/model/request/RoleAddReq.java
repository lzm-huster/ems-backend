package com.ems.usercenter.model.request;

import lombok.Data;

import java.util.List;
@Data
public class RoleAddReq {
    private String roleName;
    private String roleDescription;
    private List<Integer> permissionIdList;
}
