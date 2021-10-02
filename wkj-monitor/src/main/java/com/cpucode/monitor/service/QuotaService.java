package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.dto.DeviceInfoDTO;
import com.cpucode.monitor.entity.QuotaEntity;

import java.util.List;
import java.util.Map;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:38
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface QuotaService extends IService<QuotaEntity> {
    /**
     * 获取所有报文主题
     * @return
     */
    List<String> getAllSubject();

    /**
     * 解析报文
     * @param topic  主题名称
     * @param payloadMap  报文内容
     * @return    设备（含指标列表）
     */
    DeviceInfoDTO analysis(String topic, Map<String, Object> payloadMap);
}
