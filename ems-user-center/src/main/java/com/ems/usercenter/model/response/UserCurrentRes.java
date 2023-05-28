package com.ems.usercenter.model.response;


import com.baomidou.mybatisplus.annotation.TableField;
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
     * 学号/工号
     */

    private String IDNumber;
    /**
     * 性别
     */
    private String gender;
    /**
     * 用户手机号
     */

    private String phoneNumber;

    /**
     * 用户头像
     */

    private String avatar;
    /**
     * 所属部门
     */
    private String department;
    /***
     * 邮箱
     */
    private String email;
    /**
     * 角色列表
     */
    private List<String> roleList;
//    /**
//     * 菜单权限列表
//     */
//    private List<PermissionSimpleRes> menuPermissionList;

    /**
     * 权限列表
     */
    private List<String> userPermissionList;

    private static final long serialVersionUID = 1L;
}
