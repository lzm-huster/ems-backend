package com.ems.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.usercenter.model.entity.RolePermission;
import com.ems.usercenter.model.entity.UserRole;
import com.github.jeffreyning.mybatisplus.base.MppBaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
* @author 龙志明
* @description 针对表【RolePermission(角色权限表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.RolePermission
*/
@Mapper
public interface RolePermissionMapper extends MppBaseMapper<RolePermission> {
    @Delete("delete from RolePermission where RoleID = #{roleId}")
    boolean removeAllRolePermission(Integer roleId);
}




