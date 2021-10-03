package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.dto.*;
import com.cpucode.monitor.entity.AlarmEntity;
import com.cpucode.monitor.influx.InfluxRepository;
import com.cpucode.monitor.mapper.AlarmMapper;
import com.cpucode.monitor.service.AlarmService;
import com.cpucode.monitor.vo.Pager;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/2 20:51
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService{
    @Autowired
    private InfluxRepository influxRepository;

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

    /**
     * 查询告警日志
     * @param page 页数
     * @param pageSize 页码
     * @param start 开始时间
     * @param end 结束时间
     * @param alarmName 警告名
     * @param deviceId 设备id
     * @return
     */
    @Override
    public Pager<QuotaAllInfo> queryAlarmLog(Long page, Long pageSize,
                                      String start, String end,
                                      String alarmName, String deviceId){
        //1.where条件查询语句部分构建
        StringBuilder whereQl = new StringBuilder("where alarm = '1'");
        if(!Strings.isNullOrEmpty(start)){
            whereQl.append("and time >= '" + start + "' ");
        }
        if(!Strings.isNullOrEmpty(end)){
            whereQl.append("and time <= '" + end + "' ");
        }

        // 全模糊搜索
        if(!Strings.isNullOrEmpty(alarmName)){
            whereQl.append("and alarmName = ~/" + alarmName + "/ ");
        }
        // 最左匹配模糊查询
        if(!Strings.isNullOrEmpty(deviceId)){
            whereQl.append("and deviceId = ~/^" + deviceId + "/ ");
        }

        //2.查询记录语句
        StringBuilder listQl = new StringBuilder("select * from quota ");
        listQl.append(whereQl.toString());
        listQl.append("order by desc limit " + pageSize +
                " offset " + (page - 1) * pageSize);

        //3.查询记录数语句
        StringBuilder countQl = new StringBuilder("select count(value) from quota");
        countQl.append(whereQl.toString());

        //4.执行查询记录语句
        List<QuotaAllInfo> quotaList = influxRepository.query(listQl.toString(), QuotaAllInfo.class);

        // 添加时间格式处理
        for (QuotaAllInfo quotaAllInfo : quotaList){
            //2020-09-19T09:58:34.926Z   DateTimeFormatter.ISO_OFFSET_DATE_TIME
            //转换为 2020-09-19 09:58:34  格式
            LocalDateTime dateTime = LocalDateTime.parse(quotaAllInfo.getTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String time = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            quotaAllInfo.setTime(time);
        }

        //5.执行统计语句
        List<QuotaCount> quotaCount = influxRepository.query(countQl.toString(), QuotaCount.class);

        //6.返回结果封装
        if(quotaCount == null || quotaCount.size() <= 0){
            Pager<QuotaAllInfo> pager = new Pager<>(0L, 0L);
            pager.setPage(0);
            pager.setItems(Lists.newArrayList());

            return pager;
        }

        //记录数
        Long totalCount = quotaCount.get(0).getCount();
        Pager<QuotaAllInfo> pager = new Pager<>(totalCount, pageSize);
        pager.setPage(page);
        pager.setItems(quotaList);

        return pager;
    }
}
