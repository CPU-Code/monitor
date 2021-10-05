package com.cpucode.monitor.service.impl;

import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.service.NoticeService;
import com.cpucode.monitor.util.HttpUtil;
import com.google.common.base.Strings;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/5 20:02
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public class NoticeServiceImpl implements NoticeService {
    /**
     * 指标数据透传
     * @param quotaDTOList
     */
    @Override
    public void quotaTransfer(List<QuotaDTO> quotaDTOList) {
        for( QuotaDTO quotaDTO:quotaDTOList ){
            if(!Strings.isNullOrEmpty(quotaDTO.getWebhook())){
                //如果钩子非空，则做数据透传
                HttpUtil.httpPost(quotaDTO.getWebhook(), quotaDTO);
            }
        }
    }
}
