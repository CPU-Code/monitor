package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 面板指标数据
 * @author : cpucode
 * @date : 2021/10/4 23:22
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class BoardQuotaData implements Serializable {
    /**
     * 名称（设备编号）
     */
    private String name;

    /**
     * 指标数据
     */
    private List<Double> data;
}
