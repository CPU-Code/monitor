package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.dto.DeviceLocation;
import com.cpucode.monitor.entity.GPSEntity;

import java.util.Map;

/**
 * @author : cpucode
 * @date : 2021/10/5 9:51
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface GpsService extends IService<GPSEntity> {
    /**
     *修改gps指标
     * @param gpsEntity
     * @return
     */
    boolean update(GPSEntity gpsEntity);

    /**
     * 获取GPS 信息
     * @return
     */
    GPSEntity getGps();

    /**
     * 解析报文获得GPS信息
     *
     * @param topic  主题
     * @param payloadMap  报文内容
     * @return
     */
    DeviceLocation analysis(String topic, Map<String, Object> payloadMap);
}
