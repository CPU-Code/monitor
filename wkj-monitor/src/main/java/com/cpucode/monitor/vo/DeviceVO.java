package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : cpucode
 * @date : 2021/10/1 17:19
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class DeviceVO implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 设备号
     */
    private String sn;
    /**
     * 标签
     */
    private String tags;

    /**
     * 状态
     */
    private Boolean status;
}
