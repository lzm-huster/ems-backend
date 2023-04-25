package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.DeviceMaintenanceRecord;
import generator.service.DeviceMaintenanceRecordService;
import generator.mapper.DeviceMaintenanceRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【DeviceMaintenanceRecord(设备保养表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceMaintenanceRecordServiceImpl extends ServiceImpl<DeviceMaintenanceRecordMapper, DeviceMaintenanceRecord>
    implements DeviceMaintenanceRecordService{

}




