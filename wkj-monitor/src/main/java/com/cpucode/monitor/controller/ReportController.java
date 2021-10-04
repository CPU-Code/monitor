package com.cpucode.monitor.controller;

import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.vo.MonitorVO;
import com.cpucode.monitor.vo.PieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
