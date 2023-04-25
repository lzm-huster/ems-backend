package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.RolePermission;
import generator.service.RolePermissionService;
import generator.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【RolePermission(角色权限表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService{

}




