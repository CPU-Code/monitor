package com.cpucode.monitor.emq;

import com.cpucode.monitor.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息回调类
 *
 * @author : cpucode
 * @date : 2021/10/2 14:23
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Slf4j
public class EmqMsgProcess implements MqttCallback {
    @Autowired
    private QuotaService quotaService;

    @Autowired
    private EmqClient emqClient;

    /**
     * 连接丢失时调用
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("emq connect lost");

        //当连接丢失时再次连接emq
        emqClient.connect();

        //重新订阅所有主题
        quotaService.getAllSubject().forEach(s -> {
            //共享订阅模式
            try {
                emqClient.subscribe("$queue/" + s);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 回调消息
     * @param s 主题
     * @param mqttMessage  消息
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String payload = new String(mqttMessage.getPayload());

        System.out.println("接收到数据：" + payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
