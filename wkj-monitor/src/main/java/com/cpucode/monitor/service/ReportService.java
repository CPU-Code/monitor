package com.cpucode.monitor.service;

import com.cpucode.monitor.dto.TrendPoint;
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

    /**
     * 获取异常趋势指标
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param type 时间统计类型(1:60分钟之内, 2:当天24小时, 3:7天内)
     * @return
     */
    List<TrendPoint> getAlarmTrend(String start, String end, int type);
}
