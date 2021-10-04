package com.cpucode.monitor.vo;

import lombok.Data;

import java.util.List;

/**
 * 预览报表传入参数
 *
 * @author : cpucode
 * @date : 2021/10/4 23:34
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class PreviewVO {
    /**
     * 指标id
     */
    private String quotaId;
    /**
     * 设备id集合
     */
    private List<String> deviceIdList;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 开始时间
     */
    private String start;
    /**
     * 结束时间
     */
    private String end;
}
