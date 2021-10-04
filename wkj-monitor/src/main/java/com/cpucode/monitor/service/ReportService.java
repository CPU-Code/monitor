package com.cpucode.monitor.service;

import com.cpucode.monitor.vo.PieVO;

import java.util.List;

/**
 * 报表服务
 *
 * @author : cpucode
 * @date : 2021/10/4 15:59
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface ReportService {
    /**
     * 设备状态分布
     * @return
     */
    List<PieVO> getStatusCollect();
}
