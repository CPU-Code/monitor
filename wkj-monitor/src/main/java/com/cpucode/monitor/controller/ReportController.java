package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.HeapPoint;
import com.cpucode.monitor.dto.TrendPoint;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.vo.LineVO;
import com.cpucode.monitor.vo.MonitorVO;
import com.cpucode.monitor.vo.PieVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/4 16:06
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @Autowired
    private ESRepository esRepository;

    /**
     * 设备状态分布
     * @return
     */
    public List<PieVO> getStatusCollect(){
        return reportService.getStatusCollect();
    }


    /**
     * 获取实时监控数据
     * @return
     */
    @GetMapping("/monitor")
    public MonitorVO getMonitorData(){
        MonitorVO monitor = new MonitorVO();
        monitor.setDeviceCount(esRepository.getAllDeviceCount());
        monitor.setAlarmCount(esRepository.getAlarmCount());

        return monitor;
    }

    /**
     * 获取告警趋势
     * @return
     */
    @GetMapping("/trend/{startTime}/{endTime}/{type}")
    public LineVO getQuotaTrendCollect(@PathVariable String startTime,
                                       @PathVariable String endTime,
                                       @PathVariable Integer type){
        List<TrendPoint> trendPoints = reportService.getAlarmTrend(startTime, endTime, type);

        LineVO lineVO = new LineVO();
        lineVO.setXdata(Lists.newArrayList());
        lineVO.setSeries(Lists.newArrayList());

        trendPoints.forEach(t ->{
            lineVO.getXdata().add(formatTime(t.getTime(), type));
            lineVO.getSeries().add(t.getPointValue().longValue());
        });

        return lineVO;
    }

    /**
     * 格式化日期串
     * @param time 时间
     * @param type 类型
     * @return
     */
    private String formatTime(String time,int type){
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        if (type == 1){
            return localDateTime.getMinute() + "";
        }else if (type == 2){
            return localDateTime.getHour() + "";
        }else if (type == 3){
            return localDateTime.getMonthValue() + "月" +
                    localDateTime.getDayOfMonth() + "日";
        }

        return time;
    }

    /**
     * 获取一定时间范围之内的报警次数最多的设备指标
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping("/top10Alarm/{startTime}/{endTime}")
    public List<HeapPoint> getTop10Alarm(@PathVariable String startTime,
                                         @PathVariable String endTime){
        return reportService.getTop10Alarm(startTime, endTime);
    }
}
