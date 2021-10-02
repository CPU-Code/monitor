package com.cpucode.monitor.emq;

import com.cpucode.monitor.config.EmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : cpucode
 * @date : 2021/10/2 11:31
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
@Slf4j
public class EmqClient {
    /**
     * emq配置
     */
    @Autowired
    private EmqConfig emqConfig;

    private MqttClient mqttClient;

    /**
     * 连接mqtt broker
     */
    public void connect(){
        try {
            // 配置连接参数
            mqttClient = new MqttClient(emqConfig.getMqttServerUrl(),
                    "monitor" + UUID.randomUUID().toString());
            // 连接
            mqttClient.connect();
        }catch (MqttException e){
            log.error("mqtt creat error", e);
        }
    }


    /**
     * 发布消息
     * @param topic 消息主题
     * @param msg  发送的消息
     */
    public void publish(String topic, String msg){
        try {
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
            //向某主题发送消息
            mqttClient.getTopic(topic).publish(mqttMessage);
        } catch (MqttException e) {
            log.error("mqtt publish msg error",e);
        }
    }

}
