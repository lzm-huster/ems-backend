package com.ems.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ems.usercenter.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 龙志明
* @description 针对表【User(用户表)】的数据库操作Mapper
* @createDate 2023-04-24 09:03:00
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




