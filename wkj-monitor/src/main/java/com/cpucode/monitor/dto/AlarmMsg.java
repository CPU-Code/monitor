package com.cpucode.monitor.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.io.Serializable;

/**
 * 告警信息封装类
 *
 * @author : cpucode
 * @date : 2021/10/5 23:29
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class AlarmMsg implements Serializable {
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 告警名称
     */
    private String alarmName;

    /**
     * 指标名称
     */
    private String quotaName;

    /**
     * 文本值
     */
    private String stringValue;

    /**
     * 指标值
     */
    private Double value;
    /**
     * 告警级别
     */
    private Integer level;
    /**
     * 联网状态
     */
    private Boolean online;
    /**
     * 是否告警
     */
    private Boolean alarm;
}
