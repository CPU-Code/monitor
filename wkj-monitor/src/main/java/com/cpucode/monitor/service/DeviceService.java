package com.cpucode.monitor.service;

/**
 * @author : cpucode
 * @date : 2021/10/1 17:10
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface DeviceService {
    /**
     * 更改设备状态
     * @param deviceId 设备id
     * @param status 设备状态
     * @return
     */
    boolean setStatus(String deviceId, Boolean status);

    /**
     * 更新设备标签
     * @param deviceId 设备id
     * @param tags 设备标签
     * @return
     */
    boolean updateTags(String deviceId,String tags);
}
