package com.ems.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.usercenter.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;


/**
* @author 龙志明
* @description 针对表【Permission(权限表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.Permission
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}




