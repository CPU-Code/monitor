package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 实时监控数据
 * @author : cpucode
 * @date : 2021/10/4 16:22
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class MonitorVO implements Serializable {
    /**
     * 设备数量
     */
    private Long deviceCount;

    /**
     * 告警设备数
     */
    private Long alarmCount;
}
