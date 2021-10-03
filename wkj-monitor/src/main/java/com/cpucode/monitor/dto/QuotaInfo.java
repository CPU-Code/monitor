package com.cpucode.monitor.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * influxDB 设备警告字段
 * @author : cpucode
 * @date : 2021/10/3 16:50
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Measurement(name = "quota")
public class QuotaInfo {
    /**
     * 设备id
     */
    @Column(name = "deviceId", tag = true)
    private String deviceId;

    /**
     * 指标id
     */
    @Column(name = "quotaId", tag = true)
    private String quotaId;

    /**
     * 指标名称
     */
    @Column(name = "quotaName", tag = true)
    private String quotaName;

    /**
     * 是否告警  0：不告警  1：告警
     */
    @Column(name = "alarm", tag = true)
    private String alarm;

    /**
     * 告警级别
     */
    @Column(name = "level", tag = true)
    private String level;

    /**
     * 告警名称
     */
    @Column(name = "alarmName", tag = true)
    private String alarmName;

    /**
     * 单位
     */
    @Column(name = "unit", tag = true)
    private String unit;

    /**
     * 参考值
     */
    @Column(name = "referenceValue", tag = true)
    private String referenceValue;

    /**
     * 数值指标
     */
    @Column(name = "value")
    private Double value;

    /**
     * 非数值指标
     */
    @Column(name = "stringValue")
    private String stringValue;
}
