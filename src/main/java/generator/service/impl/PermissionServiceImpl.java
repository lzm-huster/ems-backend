package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Permission;
import generator.service.PermissionService;
import generator.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【Permission(权限表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




