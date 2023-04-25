package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Device;
import generator.service.DeviceService;
import generator.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

/**
* @author 龙志明
* @description 针对表【Device(设备列表)】的数据库操作Service实现
* @createDate 2023-04-24 09:03:00
*/
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService{

}




