package com.ems.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.usercenter.model.entity.User;
import com.ems.usercenter.model.response.UserDetailRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 龙志明
 * @description 针对表【User(用户表)】的数据库操作Mapper
 * @createDate 2023-04-24 09:03:00
 * @Entity generator.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    //根据UserID获取RoleName
    @Select("SELECT  r.RoleName " +
            "FROM UserRole ur " +
            "JOIN Role r ON ur.RoleID = r.RoleID " +
            "WHERE ur.UserID = #{UserID} and r.IsDeleted = 0 and ur.IsDeleted = 0;")
    String getRoleNameByUserID(int UserID);

    @Select("select u.*, r.RoleName,r.RoleDescription " +
            "from User u " +
            "join UserRole ur on u.UserID = ur.UserID " +
            "join Role r on ur.RoleID = r.RoleID " +
            "where u.UserID = #{userId} And u.IsDeleted = 0 And r.IsDeleted = 0 And ur.IsDeleted = 0;")
    UserDetailRes getUserDetail(int userId);
    @Select("select u.*,r.RoleID, r.RoleName,r.RoleDescription " +
            "from User u " +
            "join UserRole ur on u.UserID = ur.UserID " +
            "join Role r on ur.RoleID = r.RoleID " +
            "where r.IsDeleted = 0 And u.IsDeleted = 0 And ur.IsDeleted = 0")
    List<UserDetailRes> getAllDetail();

    @Select("select u.*, r.RoleName, r.RoleDescription " +
            "from User u " +
            "join UserRole ur on u.UserID = ur.UserID " +
            "join Role r ON r.RoleID = ur.RoleID " +
            "where ur.RoleID = 3 And r.IsDeleted = 0 And u.IsDeleted = 0 AND r.IsDeleted = 0 And ur.IsDeleted = 0;")
    List<UserDetailRes> getStaffList();
}




