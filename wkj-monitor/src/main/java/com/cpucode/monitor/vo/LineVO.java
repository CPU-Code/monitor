package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 折线图封装类
 *
 * @author : cpucode
 * @date : 2021/10/4 17:01
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class LineVO implements Serializable {
    /**
     * x轴
     */
    private List<String> xdata;

    /**
     * 数据
     */
    private List<Long> series;
}
