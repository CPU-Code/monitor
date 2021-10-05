package com.cpucode.monitor.service;

import com.cpucode.monitor.dto.QuotaDTO;

import java.util.List;

/**
 * 通知(透传)服务
 *
 * @author : cpucode
 * @date : 2021/10/5 20:01
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface NoticeService {
    /**
     * 指标数据透传
     * @param quotaDTOList
     */
    void quotaTransfer(List<QuotaDTO> quotaDTOList);
}
