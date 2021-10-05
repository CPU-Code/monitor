package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.dto.DeviceLocation;
import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.entity.GPSEntity;
import com.cpucode.monitor.mapper.GpsMapper;
import com.cpucode.monitor.service.GpsService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : cpucode
 * @date : 2021/10/5 9:51
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class GpsServiceImpl extends ServiceImpl<GpsMapper, GPSEntity> implements GpsService {
    @Autowired
    private EmqClient emqClient;

    /**
     * 修改gps定义时重新订阅主题
     * @param gpsEntity
     * @return
     */
    @Override
    public boolean update(GPSEntity gpsEntity) {
        //订阅主题
        try {
            emqClient.subscribe("$queue/" + gpsEntity.getSubject());
        } catch (MqttException e) {
            log.error("subscribe error",e);
        }

        // TODO
        gpsEntity.setId(1);

        return this.updateById(gpsEntity);
    }

    /**
     * 获取GPS 信息
     * @return
     */
    @Override
    public GPSEntity getGps(){
        return this.getById(1);
    }

    /**
     * 解析报文获得GPS信息
     *
     * @param topic  主题
     * @param payloadMap  报文内容
     * @return
     */
    @Override
    public DeviceLocation analysis(String topic, Map<String, Object> payloadMap){
        //读取规则
        GPSEntity gpsEntity = getGps();

        if (gpsEntity == null){
            return null;
        }
        if (Strings.isNullOrEmpty(gpsEntity.getSubject())){
            // 主题为空
            return null;
        }
        if (!topic.equals(gpsEntity.getSubject())){
            //如果主题不匹配
            return null;
        }

        //读取设备id
        String deviceId = "";
        deviceId = (String) payloadMap.get(gpsEntity.getSnKey());
        if (Strings.isNullOrEmpty(deviceId)){
            return null;
        }

        //提取gps
        String location = "";
        if (gpsEntity.getSingleField()){
            //如果是单字段
            location = ((String)payloadMap.get(gpsEntity.getValueKey()))
                    .replace(gpsEntity.getSeparation(), ",");
        }else {
            //如果是双字段
            location = payloadMap.get(gpsEntity.getLongitude()) + "," +
                    payloadMap.get(gpsEntity.getLatitude());
        }

        //封装返回结果
        if(location != null){
            DeviceLocation deviceLocation = new DeviceLocation();
            deviceLocation.setDeviceId(deviceId);
            deviceLocation.setLocation(location);

            return deviceLocation;
        }else {
            return null;
        }
    }
}
