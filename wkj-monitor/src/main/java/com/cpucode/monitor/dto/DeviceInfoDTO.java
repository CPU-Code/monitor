package com.cpucode.monitor.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/2 16:02
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceInfoDTO {
    /**
     *设备
     */
    private DeviceDTO device;

    /**
     *指标列表
     */
    private List<QuotaDTO> quotaList;
}
