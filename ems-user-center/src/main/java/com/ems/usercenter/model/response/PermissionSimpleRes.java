package com.ems.usercenter.model.response;

import lombok.Data;

@Data
public class PermissionSimpleRes {
    /**
     * 权限编号
     */
    private Integer permissionID;

    /**
     * 权限名
     */
    private String permissionName;

    /**
     * 权限描述
     */

    private String permissionDescription;
}
