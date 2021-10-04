package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 面板VO对象
 * @author : cpucode
 * @date : 2021/10/4 23:23
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class BoardQuotaVO implements Serializable {
    /**
     * x轴数据
     */
    private List<String> xdata;

    /**
     * Y轴数据
     */
    private List<BoardQuotaData> series;

    /**
     * 面板名称
     */
    private String name;
}
