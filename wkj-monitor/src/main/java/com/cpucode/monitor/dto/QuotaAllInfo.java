package com.cpucode.monitor.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * 时间字段的指标对象
 *
 * @author : cpucode
 * @date : 2021/10/3 17:51
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Measurement(name = "quota")
public class QuotaAllInfo extends QuotaInfo {
    @Column(name = "time")
    private String time;
}
