package com.cpucode.monitor.service;

import com.cpucode.monitor.dto.HeapPoint;
import com.cpucode.monitor.dto.TrendPoint;
import com.cpucode.monitor.dto.TrendPoint2;
import com.cpucode.monitor.vo.BoardQuotaVO;
import com.cpucode.monitor.vo.Pager;
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

    /**
     * 获取一定时间范围之内的报警次数最多的设备指标
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<HeapPoint> getTop10Alarm(String startTime, String endTime);

    /**
     * 通过指标获取关联设备
     * @param page 页数
     * @param pageSize 页码
     * @param quotaId 指标id
     * @return
     */
    Pager<String> getDeviceByQuota(Long page, Long pageSize, String quotaId);

    /**
     * 获取指标趋势
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param quotaId 指标Id
     * @param type 时间统计类型(1:60分钟之内,2:当天24小时,3:7天内)
     * @param deviceId 设备编码
     * @return
     */
    List<TrendPoint2> getQuotaTrend(String start, String end, String quotaId, String deviceId, int type);

    /**
     * 指标趋势图
     * @param quotaId 指标id
     * @param deviceIds 设备id集合
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 类型
     * @return
     */
    BoardQuotaVO getBoardData(String quotaId, List<String> deviceIds, String startTime, String endTime, Integer type);
}
