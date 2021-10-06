package com.cpucode.monitor.service.impl;

import com.cpucode.common.SystemDefinition;
import com.cpucode.monitor.config.WebHookConfig;
import com.cpucode.monitor.dto.AlarmMsg;
import com.cpucode.monitor.dto.DeviceLocation;
import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.service.NoticeService;
import com.cpucode.monitor.util.HttpUtil;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : cpucode
 * @date : 2021/10/5 20:02
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebHookConfig webHookConfig;

    @Autowired
    private EmqClient emqClient;

    /**
     * 指标数据透传
     * @param quotaDTOList
     */
    @Override
    public void quotaTransfer(List<QuotaDTO> quotaDTOList) {
        for( QuotaDTO quotaDTO : quotaDTOList ){
            if(!Strings.isNullOrEmpty(quotaDTO.getWebhook())){
                //如果钩子非空，则做数据透传
                HttpUtil.httpPost(quotaDTO.getWebhook(), quotaDTO);
            }

            if ("1".equals(quotaDTO.getAlarm()) && !Strings.isNullOrEmpty(quotaDTO.getAlarmWebHook())){
                // key: XXXXX_设备id_告警名称
                String key = SystemDefinition.CYCLE_KEY + quotaDTO.getDeviceId() + ":" + quotaDTO.getAlarmName();

                if(redisTemplate.boundValueOps(key).get() == null){
                    HttpUtil.httpPost(quotaDTO.getAlarmWebHook(), quotaDTO);

                    redisTemplate.boundValueOps(key).set(quotaDTO.getStringValue(), quotaDTO.getCycle(), TimeUnit.MINUTES);
                }

            }

            //报警前端推送
            sendAlarm(quotaDTO);
        }
    }

    /**
     * 断连透传
     * @param deviceId
     * @param online
     */
    @Override
    public void onlineTransfer(String deviceId, Boolean online){
        if(!Strings.isNullOrEmpty(webHookConfig.getOnline())){
            Map<String, Object> map = Maps.newHashMap();
            map.put("deviceId", deviceId);
            map.put("online", online);

            HttpUtil.httpPost(webHookConfig.getOnline(), map);
        }

        if(!online){
            disconnectionAlarm(deviceId);
        }
    }

    /**
     * gps透传
     * @param deviceLocation
     */
    @Override
    public void gpsTransfer(DeviceLocation deviceLocation){
        if(!Strings.isNullOrEmpty(webHookConfig.getGps())){
            HttpUtil.httpPost(webHookConfig.getGps(), deviceLocation);
        }
    }

    /**
     * 告警前端推送
     * @param quotaDTO
     */
    private void sendAlarm(QuotaDTO quotaDTO){
        if (!"1".equals(quotaDTO.getAlarm())){
            return;
        }

        AlarmMsg alarmMsg = new AlarmMsg();
        BeanUtils.copyProperties(quotaDTO, alarmMsg);
        alarmMsg.setLevel(Integer.parseInt(quotaDTO.getLevel()));
        alarmMsg.setOnline(true);

        //发送到 emq
        try {
            emqClient.publish("/device/alarm", JsonUtil.serialize(alarmMsg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断网警告
     * @param deviceId
     */
    private void disconnectionAlarm(String deviceId){
        //以web开头的client为系统前端, monitor开头的是物联网中台服务端
        if(deviceId.startsWith("webclient") || deviceId.startsWith("monitor")){
            return;
        }

        AlarmMsg alarmMsg = new AlarmMsg();
        alarmMsg.setLevel(1);
        alarmMsg.setAlarmName("设备断网");
        alarmMsg.setDeviceId(deviceId);
        alarmMsg.setOnline(false);

        //发送到emq
        try {
            emqClient.publish("/device/alarm", JsonUtil.serialize(alarmMsg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
