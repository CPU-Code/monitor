package com.cpucode.monitor.service.impl;

import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.vo.PieVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/4 16:00
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ESRepository esRepository;

    /**
     * 获取设备的各种状态总和
     * @return
     */
    @Override
    public List<PieVO> getStatusCollect() {
        //全部设备数量
        Long allDeviceCount = esRepository.getAllDeviceCount();
        //离线设备数量
        Long offlineCount = esRepository.getOfflineCount();
        //报警设备数量
        Long alarmCount = esRepository.getAlarmCount();

        PieVO devicePie = new PieVO();
        devicePie.setName("正常运转");
        devicePie.setValue(allDeviceCount - offlineCount - alarmCount);

        PieVO offlinePie = new PieVO();
        offlinePie.setName("离线");
        offlinePie.setValue(offlineCount);

        PieVO alarmPie = new PieVO();
        offlinePie.setName("警告");
        offlinePie.setValue(alarmCount);

        List<PieVO> pieVOList= Lists.newArrayList();
        pieVOList.add(devicePie);
        pieVOList.add(offlinePie);
        pieVOList.add(alarmPie);

        return pieVOList;
    }
}
