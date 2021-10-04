package com.cpucode.monitor.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * 趋势指标点
 *
 * @author : cpucode
 * @date : 2021/10/4 16:40
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Measurement(name = "quota")
public class TrendPoint implements Serializable {
    /**
     * 时间
     */
    @Column(name = "time")
    private String time;

    /**
     * 时间点数据
     */
    @Column(name = "pointValue")
    private Integer pointValue;
}
