package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.ApprovalRecord;
import generator.service.ApprovalRecordService;
import generator.mapper.ApprovalRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【ApprovalRecord(申请审批记录表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class ApprovalRecordServiceImpl extends ServiceImpl<ApprovalRecordMapper, ApprovalRecord>
    implements ApprovalRecordService{

}




