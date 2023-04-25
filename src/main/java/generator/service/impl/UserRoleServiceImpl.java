package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.UserRole;
import generator.service.UserRoleService;
import generator.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【UserRole(用户角色表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




