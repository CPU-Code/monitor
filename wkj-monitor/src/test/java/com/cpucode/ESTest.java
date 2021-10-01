package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
     * 插入数据
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

    /**
     * 根据设备id 查询数据
     * wkj-monitor/src/main/java/com/cpucode/monitor/es/ESRepository/searchDeviceById
     */
    @Test
    public void testSearchById(){
        // 根据 设备id 查询数据
        DeviceDTO deviceDTO = esRepository.searchDeviceById("1111");

        try {
            // 转成json
            String json = JsonUtil.serialize(deviceDTO);

            // 打印
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新设备告警信息
     * wkj-monitor/src/main/java/com/cpucode/monitor/es/ESRepository/testAlarm
     */
    @Test
    public void testAlarm(){
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId("1111");
        deviceDTO.setAlarm(true);
        deviceDTO.setLevel(1);
        deviceDTO.setAlarmName("温度过高");

        esRepository.updateDevicesAlarm(deviceDTO);
    }
}
