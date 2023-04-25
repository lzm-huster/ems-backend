package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.TaskInfo;
import generator.service.TaskInfoService;
import generator.mapper.TaskInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【TaskInfo(通知任务表)】的数据库操作Service实现
* @createDate 2023-04-24 10:54:52
*/
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo>
    implements TaskInfoService{

}




