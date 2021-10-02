package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.dto.DeviceInfoDTO;
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

    /**
     *  根据设备信息判断
     * @param deviceInfoDTO
     * @return
     */
    @Override
    public DeviceInfoDTO verifyDeviceInfo(DeviceInfoDTO deviceInfoDTO) {
        // 封装指标的告警  封装设备的告警
        DeviceDTO deviceDTO = deviceInfoDTO.getDevice();

        //假设不告警
        deviceDTO.setAlarm(false);
        deviceDTO.setAlarmName("正常");
        deviceDTO.setLevel(0);
        deviceDTO.setOnline(true);
        deviceDTO.setStatus(true);

        for (QuotaDTO quotaDTO : deviceInfoDTO.getQuotaList()){
            //根据指标得到告警信息
            AlarmEntity alarmEntity = verifyQuota(quotaDTO);

            if (alarmEntity != null){
                //如果指标存在告警
                quotaDTO.setAlarm("1");
                //告警名称
                quotaDTO.setAlarmName(alarmEntity.getName());
                //告警级别
                quotaDTO.setLevel(alarmEntity.getLevel() + "");
                //告警web钩子
                quotaDTO.setAlarmWebHook(alarmEntity.getWebHook());
                //沉默周期
                quotaDTO.setCycle(alarmEntity.getCycle());

                //存储设备告警信息
                if(alarmEntity.getLevel().intValue() > deviceDTO.getLevel().intValue()){
                    deviceDTO.setLevel(alarmEntity.getLevel());
                    deviceDTO.setAlarm(true);
                    deviceDTO.setAlarmName(alarmEntity.getName());
                }
            }else {
                //如果指标不存储在告警
                quotaDTO.setAlarm("0");
                quotaDTO.setAlarmName("正常");
                quotaDTO.setLevel("0");
                quotaDTO.setAlarmWebHook("");
                quotaDTO.setCycle(0);
            }
        }

        return deviceInfoDTO;
    }
}
