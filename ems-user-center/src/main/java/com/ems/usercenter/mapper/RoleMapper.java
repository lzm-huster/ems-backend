package com.ems.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.usercenter.model.entity.Role;
import com.ems.usercenter.model.response.RoleSimpleRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
* @author 龙志明
* @description 针对表【Role(角色表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select r.RoleName " +
            "from Role r " +
            "inner join UserRole ur on r.RoleID = ur.RoleID " +
            "where ur.UserID = #{userId} and ur.IsDeleted = 0 and r.IsDeleted = 0")
    List<String> getRoleListByUserId(Integer userId);
    @Select("select r.RoleID,r.RoleName,r.RoleDescription from Role r")
    List<RoleSimpleRes> getRoleList();


}




