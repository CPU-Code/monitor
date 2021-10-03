package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.QuotaAllInfo;
import com.cpucode.monitor.service.AlarmService;
import com.cpucode.monitor.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cpucode
 * @date : 2021/10/3 23:22
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/alarm")
public class AlarmController {
    @Autowired
    AlarmService alarmService;

    /**
     * 查询告警日志
     * @param page 页数
     * @param pageSize 页码
     * @param start 开始时间
     * @param end 结束时间
     * @param alarmName 警告名
     * @param deviceId 设备id
     * @return
     */
    @GetMapping("/log")
    public Pager<QuotaAllInfo> alarmLog(@RequestParam(value = "page", required = false, defaultValue = "1")
                                                    Long page,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                Long pageSize,
                                        @RequestParam(value = "start")
                                                    String start,
                                        @RequestParam(value = "end")
                                                    String end,
                                        @RequestParam(value = "alarmName", required = false, defaultValue = "")
                                                    String alarmName,
                                        @RequestParam(value = "deviceId", required = false, defaultValue = "")
                                                    String deviceId){

        return alarmService.queryAlarmLog(page, pageSize, start, end, alarmName, deviceId);
    }
}
