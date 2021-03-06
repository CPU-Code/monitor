package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.dto.DeviceLocation;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
     * wkj-monitor/src/main/java/com/cpucode/monitor/es/ESRepository/updateDevicesAlarm
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

    /**
     * 更新设备告警信息
     * wkj-monitor/src/main/java/com/cpucode/monitor/es/ESRepository/updateOnline
     */
    @Test
    public void testOnline(){
        esRepository.updateOnline("123456",false);
    }

    /**
     * 获取总数
     */
    @Test
    public void testCount(){
        //设备总数
        Long allDeviceCount = esRepository.getAllDeviceCount();
        System.out.println("设备总数：" + allDeviceCount);

        //离线设备数量
        Long offlineCount = esRepository.getOfflineCount();
        System.out.println("离线设备：" + offlineCount);

        //告警设备数量
        Long alarmCount = esRepository.getAlarmCount();
        System.out.println("告警设备：" + alarmCount);
    }

    /**
     * 测试获取附近的设备
     */
    @Test
    public void testGEO(){
        List<DeviceLocation> deviceLocationList =esRepository.searchDeviceLocation(40.332, 32.3232, 10);

        try {
            System.out.println(JsonUtil.serialize(deviceLocationList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
