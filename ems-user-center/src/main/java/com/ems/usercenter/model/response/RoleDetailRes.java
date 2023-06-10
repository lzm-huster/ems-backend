package com.ems.usercenter.model.response;


import lombok.Data;

import java.util.List;

@Data
public class RoleDetailRes {

    private Integer roleID;

    /**
     * 角色名称
     */

    private String roleName;

    /**
     * 角色描述
     */

    private String roleDescription;

    private List<PermissionSimpleRes> permissionSimpleResListList;

}
