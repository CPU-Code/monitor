package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.dto.DeviceInfoDTO;
import com.cpucode.monitor.service.QuotaService;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : cpucode
 * @date : 2021/10/2 17:44
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class QuotaTest {
    @Autowired
    private QuotaService quotaService;

    /**
     * 解析报文测试
     */
    @Test
    public void AnalysisTest(){
        Map map = new HashMap<>();
        map.put("sn", "1111");
        map.put("temp", 22.2);

        DeviceInfoDTO deviceInfoDTO = quotaService.analysis("temperature", map);
        String json = null;

        try {
            json = JsonUtil.serialize(deviceInfoDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(json);
    }
}
