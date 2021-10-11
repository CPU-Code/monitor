package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
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
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class InfluxTest {
    @Autowired
    private InfluxRepository influxRepository;

    @Test
    public void testAdd(){
        QuotaInfo quotaInfo = new QuotaInfo();

        quotaInfo.setDeviceId("xxxxx");
        quotaInfo.setQuotaId("1");
        quotaInfo.setQuotaName("ddd");
        quotaInfo.setReferenceValue("0-10");
        quotaInfo.setUnit("摄氏度");
        quotaInfo.setAlarm("1");
        quotaInfo.setFloatValue(11.44f);
        quotaInfo.setDoubleValue(11.44D);
        quotaInfo.setIntegerValue(43);
        quotaInfo.setBoolValue(false);
        quotaInfo.setStringValue("fdsd");

        influxRepository.add(quotaInfo);
    }
}
