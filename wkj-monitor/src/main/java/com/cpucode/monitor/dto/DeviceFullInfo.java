package com.cpucode.monitor.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/5 13:07
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceFullInfo implements Serializable {
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 位置
     */
    private String location;

    /**
     * 是否在线
     */
    private Boolean online;

    /**
     * 是否警告
     */
    private Boolean alarm;

    /**
     * 警告字段
     */
    private List<QuotaInfo> quotaInfoList;

}
