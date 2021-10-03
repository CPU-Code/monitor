package com.cpucode.monitor.emq;

import com.cpucode.monitor.entity.QuotaEntity;
import com.cpucode.monitor.service.QuotaService;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author : cpucode
 * @date : 2021/10/3 23:56
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
public class Mock {
    @Autowired
    private QuotaService quotaService;

    @Autowired
    private EmqClient emqClient;

    /**
     * 设备报文数据模拟
     * 每十秒
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void addDatas(){
        System.out.println(LocalDateTime.now() + "报文数据模拟中");
        //提取指标
        List<QuotaEntity> quotaList = quotaService.list();

        //模拟10台设备
        for(int i = 0; i < 10; i++) {
            //设备编号
            String deviceId = 100000 + i + "";

            //提取指标
            for(QuotaEntity quotaEntity : quotaList){
                Map<String, Object> map = Maps.newHashMap();
                map.put(quotaEntity.getSnKey(), deviceId);
                //随机产生
                Random random = new Random();
                int quotaValue = random.nextInt(40);
                map.put(quotaEntity.getValueKey(), quotaValue);

                try {
                    String json = JsonUtil.serialize(map);
                    emqClient.publish(quotaEntity.getSubject(), json);

                    Thread.sleep(20);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
