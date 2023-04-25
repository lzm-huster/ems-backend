package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.DeviceInventoryLossRecord;
import generator.service.DeviceInventoryLossRecordService;
import generator.mapper.DeviceInventoryLossRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【DeviceInventoryLossRecord(设备盘亏表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceInventoryLossRecordServiceImpl extends ServiceImpl<DeviceInventoryLossRecordMapper, DeviceInventoryLossRecord>
    implements DeviceInventoryLossRecordService{

}




