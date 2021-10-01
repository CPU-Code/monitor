package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 指标配置表视图
 *
 * @author : cpucode
 * @date : 2021/10/1 15:41
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class QuotaVO implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标单位
     */
    private String unit;

    /**
     * 报文主题
     */
    private String subject;

    /**
     * 指标值字段
     */
    private String valueKey;

    /**
     * 设备识别码字段(设备Id)
     */
    private String snKey;

    /**
     * web钩子地址
     */
    private String webhook;

    /**
     * 指标字段类型，Double、Inteter、Boolean
     */
    private String valueType;

    /**
     * 参考值
     */
    private String referenceValue;
}
