package com.cpucode.monitor.controller;

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
            lineVO.getXdata().add(t.getTime());
            lineVO.getSeries().add(t.getPointValue().longValue());
        });

        return lineVO;
    }
}
