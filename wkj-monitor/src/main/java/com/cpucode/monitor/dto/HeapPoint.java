package com.cpucode.monitor.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * 累积指标
 *
 * @author : cpucode
 * @date : 2021/10/4 18:49
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Measurement(name = "quota")
public class HeapPoint implements Serializable {
    @Column(name = "deviceId")
    private String deivceId;

    @Column(name = "heapValue")
    private Double heapValue;

    @Column(name = "quotaId")
    private String quotaId;

    @Column(name = "quotaName")
    private String quotaName;
}
