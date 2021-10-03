package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.dto.DeviceInfoDTO;
import com.cpucode.monitor.dto.QuotaAllInfo;
import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.entity.AlarmEntity;
import com.cpucode.monitor.vo.Pager;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/2 18:03
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface AlarmService extends IService<AlarmEntity> {
    /**
     * 根据指标判断告警信息
     * @param quotaDTO 指标DTO
     */
    AlarmEntity verifyQuota(QuotaDTO quotaDTO);

    /**
     * 获取报警配置集合
     * @param quotaId 指标id
     * @return
     */
    public List<AlarmEntity> getByQuotaId(Integer quotaId);

    /**
     * 根据设备信息判断
     * @param deviceInfoDTO
     */
    DeviceInfoDTO verifyDeviceInfo(DeviceInfoDTO deviceInfoDTO);

    /**
     * 查询告警日志
     * @param page 页数
     * @param pageSize 页码
     * @param start 开始数据
     * @param end 结束数据
     * @param alarmName 警告名
     * @param deviceId 设备id
     * @return
     */
    Pager<QuotaAllInfo> queryAlarmLog(Long page, Long pageSize,
                                      String start, String end,
                                      String alarmName, String deviceId);
}
