package com.cpucode.monitor.core;

import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 自动监控
 *
 * @author : cpucode
 * @date : 2021/10/2 14:26
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
@Slf4j
public class Monitor {
    @Autowired
    private EmqClient emqClient;

    @Autowired
    private QuotaService quotaService;

    /**
     * 初始化 , 订阅所有主题
     */
    @PostConstruct
    public void init(){
        System.out.println("初始化方法，订阅主题");

        emqClient.connect();

        quotaService.getAllSubject().forEach(s ->{
            // 共享队列
            try {
                emqClient.subscribe("$queue/" + s);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }
}
