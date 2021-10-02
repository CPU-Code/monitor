package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.entity.AlarmEntity;
import com.cpucode.monitor.mapper.AlarmMapper;
import com.cpucode.monitor.service.AlarmService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/2 20:51
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService{

    /**
     * 告警信息
     * @param quotaDTO 指标DTO
     * @return
     */
   @Override
   public AlarmEntity verifyQuota(QuotaDTO quotaDTO){
       //1.根据指标id查询告警判断规则列表
       List<AlarmEntity> byQuotaId = getByQuotaId(quotaDTO.getId());

       for (){
           //判断：操作符和指标对比
           if (){

           }else {

           }
       }

       return null;
   }

    /**
     * 获取报警配置集合
     * @param quotaId 指标id
     * @return
     */
    @Override
    public List<AlarmEntity> getByQuotaId(Integer quotaId) {
        QueryWrapper<AlarmEntity> wrapper = new QueryWrapper<>();

        wrapper.lambda()
                .eq(AlarmEntity::getQuotaId, quotaId)
                .orderByDesc(AlarmEntity::getLevel);

        return this.list(wrapper);
    }
}
