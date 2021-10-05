package com.cpucode.monitor.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : cpucode
 * @date : 2021/10/5 10:54
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceLocation implements Serializable {
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 位置
     */
    private String location;
}
