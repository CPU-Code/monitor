package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.HeapPoint;
import com.cpucode.monitor.dto.TrendPoint;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.vo.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 通过指标获取关联设备
     * @param page 页数
     * @param pageSize 页码
     * @param quotaId 指标id
     * @return
     */
    @GetMapping("/devices")
    public Pager<String> getDeviceByQuota( @RequestParam(value = "page", required = false, defaultValue = "1")
                                                       Long page,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                   Long pageSize,
                                           @RequestParam(value = "quotaId")
                                                       String quotaId){
        return reportService.getDeviceByQuota(page, pageSize, quotaId);
    }

    /**
     * 报表预览
     * @param previewVO
     * @return
     */
    @PostMapping("/preview")
    public BoardQuotaVO getPreviewData( @RequestBody PreviewVO previewVO ){
        BoardQuotaVO boardQuotaVO = reportService.getBoardData(previewVO.getQuotaId(),
                previewVO.getDeviceIdList(),
                previewVO.getStart(),
                previewVO.getEnd(),
                previewVO.getType());

        //时间处理
        List<String> xdata = Lists.newArrayList();
        for (String x : boardQuotaVO.getXdata()){
            xdata.add(formatTime(x, previewVO.getType()));
        }

        boardQuotaVO.setXdata(xdata);

        return boardQuotaVO;
    }
}
