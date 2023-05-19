package com.ems.usercenter.model.response;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserCurrentRes implements Serializable {
    /**
     * 用户名
     */

    private String userName;

    /**
     * 用户学号
     */

    private String userNumber;

    /**
     * 用户手机号
     */

    private String userPhone;

    /**
     * 用户头像
     */

    private String userAvatar;

    /**
     * 用户QQ号
     */

    private String QQNumber;

    /**
     * 班级
     */

    private String userClass;

//    /**
//     * 菜单权限列表
//     */
//    private List<UserMenuPermissionRes> menuPermissionList;

//    /**
//     * 权限列表
//     */
//    private List<String> userPermissionList;

    private static final long serialVersionUID = 1L;
}
