package com.cpucode.monitor.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 指标DTO
 *
 * @author : cpucode
 * @date : 2021/10/2 15:58
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class QuotaDTO implements Serializable {
    /**
     * 指标ID
     */
    private Integer id;

    /**
     * 指标名称
     */
    private String quotaName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 报文主题
     */
    private String subject;

    /**
     * 指标值字段名称
     */
    private String valueKey;

    /**
     * 指标值数据类型
     */
    private String valueType;

    /**
     * 指标值（数值）
     */
    private Double value;

    /**
     * 指标值(非数值)
     */
    private String  stringValue;

    /**
     * 设备识别码字段(设备Id)
     */
    private String snKey;

    /**
     * web钩子地址
     */
    private String webhook;

    /**
     * 参考值
     */
    private String referenceValue;

    /**
     * 设备Id
     */
    private String deviceId;

    /**
     * 是否告警
     */
    private String alarm;

    /**
     * 告警名称
     */
    private String alarmName;

    /**
     * 告警级别
     */
    private String level;

    /**
     * 告警web钩子
     */
    private String alarmWebHook;

    /**
     * 沉默周期
     */
    private Integer cycle;
}
