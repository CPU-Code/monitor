package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.es.ESRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : cpucode
 * @date : 2021/10/1 16:23
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class ESTest {
    @Autowired
    private ESRepository esRepository;

    /**
     * wkj-monitor/src/main/java/com/cpucode/monitor/es/ESRepository/addDevices
     */
    @Test
    public void testAdd(){
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId("1111");
        deviceDTO.setAlarm(false);
        deviceDTO.setAlarmName("温度告警");
        deviceDTO.setLevel(0);
        deviceDTO.setOnline(true);
        deviceDTO.setTag("cpuCode");
        deviceDTO.setStatus(true);

        esRepository.addDevices(deviceDTO);
    }
}
