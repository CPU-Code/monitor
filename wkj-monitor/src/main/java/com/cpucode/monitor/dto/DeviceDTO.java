package com.cpucode.monitor.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备DTO
 *
 * @author : cpucode
 * @date : 2021/10/1 16:08
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceDTO implements Serializable {
    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 是否告警
     */
    private Boolean alarm;

    /**
     * 告警名称
     */
    private String alarmName;

    /**
     * 告警级别
     */
    private Integer level;

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 标签
     */
    private String tag;

    /**
     * 开关状态
     */
    private Boolean status;
}
