package com.cpucode.monitor.vo;

import com.cpucode.monitor.dto.QuotaInfo;
import lombok.Data;

import java.util.List;

/**
 * 设备指标详情vo
 * @author : cpucode
 * @date : 2021/10/4 0:20
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceQuotaVO {
    /**
     * 设备编号
     */
    private String deviceId;
    /**
     * 在线状态
     */
    private Boolean online;
    /**
     * 告警级别
     */
    private Integer level;

    /**
     * 指标列表
     */
    private List<QuotaInfo> quotaList;
}
