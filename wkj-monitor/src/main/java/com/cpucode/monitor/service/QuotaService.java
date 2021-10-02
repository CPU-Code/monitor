package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.entity.QuotaEntity;

import java.util.List;

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
}
