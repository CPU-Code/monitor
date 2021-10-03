package com.cpucode.monitor.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * 封装统计记录数
 *
 * @author : cpucode
 * @date : 2021/10/3 17:52
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Measurement(name = "quota")
public class QuotaCount {
    @Column(name = "count")
    private Long count;
}
