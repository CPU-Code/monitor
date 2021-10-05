package com.cpucode.monitor.core;

import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.entity.GPSEntity;
import com.cpucode.monitor.service.GpsService;
import com.cpucode.monitor.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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

    @Autowired
    private GpsService gpsService;

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
                //通过共享订阅以免影响其他系统接收消息，
                // 并且可以通过负载均衡实现消息的接收处理，
                // 以免客户端发过来的消息频率过大，服务端被击垮
                emqClient.subscribe("$queue/" + s);

                System.out.println("订阅主题：" + s);
            } catch (MqttException e) {
                log.error("订阅主题出错：" + s, e);
            }
        });

        //----------------订阅gps主题数据-------------------
        GPSEntity gpsEntity = gpsService.getGps();

        if (gpsEntity == null){
            return;
        }

        try{
            if(Strings.isNotEmpty(gpsEntity.getSubject())){
                //如果主题不为空
                emqClient.subscribe("$queue/" + gpsEntity.getSubject());
            }
        } catch (MqttException e) {
            log.error("订阅主题出错：",e);
        }
    }
}
