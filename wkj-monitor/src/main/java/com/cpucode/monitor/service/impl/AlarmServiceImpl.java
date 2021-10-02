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
     * 判断告警信息
     * @param quotaDTO 指标DTO
     * @return 报警配置
     */
   @Override
   public AlarmEntity verifyQuota(QuotaDTO quotaDTO){
       //1.根据指标id查询告警判断规则列表
       List<AlarmEntity> alarmEntityList = getByQuotaId(quotaDTO.getId());
       AlarmEntity alarm = null;

       for (AlarmEntity alarmEntity : alarmEntityList){
           //判断：操作符和指标对比
           if ("String".equals( quotaDTO.getValueType() ) ||
                   "Boolean".equals(quotaDTO.getValueType())){
                if (alarmEntity.getOperator().equals("=") &&
                        quotaDTO.getStringValue().equals(alarmEntity.getThreshold())){
                    alarm = alarmEntity;
                    break;
                }
           }else {
               //数值
               if(alarmEntity.getOperator().equals(">") &&
                       quotaDTO.getValue() > alarmEntity.getThreshold() ){
                   alarm = alarmEntity;
                   break;
               }
               if(alarmEntity.getOperator().equals("<") &&
                       quotaDTO.getValue() < alarmEntity.getThreshold() ){
                   alarm = alarmEntity;
                   break;
               }
               if(alarmEntity.getOperator().equals("=") &&
                       quotaDTO.getValue().equals(alarmEntity.getThreshold()) ){
                   alarm = alarmEntity;
                   break;
               }
           }
       }

       return alarm;
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
