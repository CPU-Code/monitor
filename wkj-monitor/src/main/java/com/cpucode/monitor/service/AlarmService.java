package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.entity.AlarmEntity;

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
}
