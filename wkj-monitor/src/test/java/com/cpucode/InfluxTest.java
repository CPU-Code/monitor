package com.cpucode;

import com.cpucode.monitor.dto.QuotaInfo;
import com.cpucode.monitor.influx.InfluxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : cpucode
 * @date : 2021/10/3 16:55
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InfluxTest {
    @Autowired
    private InfluxRepository influxRepository;

    @Test
    public void testAdd(){
        QuotaInfo quotaInfo = new QuotaInfo();

        quotaInfo.setDeviceId("123456");
        quotaInfo.setQuotaId("1");
        quotaInfo.setQuotaName("温度");
        quotaInfo.setReferenceValue("0-10");
        quotaInfo.setUnit("摄氏度");
        quotaInfo.setAlarm("1");
        quotaInfo.setValue(11D);

        influxRepository.add(quotaInfo);
    }
}