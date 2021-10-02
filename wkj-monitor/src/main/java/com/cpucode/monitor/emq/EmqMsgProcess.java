package com.cpucode.monitor.emq;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author : cpucode
 * @date : 2021/10/2 14:23
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public class EmqMsgProcess implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable) {

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
