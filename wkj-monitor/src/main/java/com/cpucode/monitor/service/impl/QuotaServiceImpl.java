package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.dto.DeviceInfoDTO;
import com.cpucode.monitor.dto.QuotaDTO;
import com.cpucode.monitor.entity.QuotaEntity;
import com.cpucode.monitor.mapper.QuotaMapper;
import com.cpucode.monitor.service.QuotaService;
import com.google.common.collect.Lists;
import org.elasticsearch.common.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:39
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class QuotaServiceImpl extends ServiceImpl<QuotaMapper, QuotaEntity> implements QuotaService {
    /**
     * 获取所有报文主题
     * @return 报文主题集合
     */
    @Override
    public List<String> getAllSubject() {
        QueryWrapper<QuotaEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(QuotaEntity::getSubject);

        return this.list(wrapper).stream()
                .map(q -> q.getSubject()).collect(Collectors.toList());
    }

    /**
     * 解析报文
     * @param topic  主题名称
     * @param payloadMap  报文内容
     * @return    设备（含指标列表）
     */
    @Override
    public DeviceInfoDTO analysis(String topic, Map<String, Object> payloadMap) {
        //1.获取指标配置
        // 根据主题查询指标列表
        List<QuotaEntity> quotaList = baseMapper.selectBySubject(topic);

        if (quotaList.size() == 0){
            return null;
        }

        //2.封装设备信息
        String snKey = quotaList.get(0).getSnKey();
        if (Strings.isNullOrEmpty(snKey)){
            return null;
        }

        // 设备编号
        String deviceId = (String) payloadMap.get(snKey);
        if (Strings.isNullOrEmpty(deviceId)) {
            return null;
        }

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(deviceId);

        //3.封装指标列表 : 循环我们根据主题名称查询得指标列表，到报文中提取，如果能够提到，进行封装
        List<QuotaDTO> quotaDTOList = Lists.newArrayList();
        for (QuotaEntity quota : quotaList) {
            //指标key
            String quotaKey = quota.getValueKey();

            if (payloadMap.containsKey(quotaKey)){
                QuotaDTO quotaDTO = new QuotaDTO();

                //复制指标配置信息
                BeanUtils.copyProperties(quota, quotaDTO);
                quotaDTO.setQuotaName(quota.getName());

                //指标值封装
                //指标分为两种  1.数值  2.非数值（string boolean）
                //1.数值   value 存储数值  stringValue :存储数值字符串
                //2.非数值  value 0   stringValue:内容

                //如果是非数值
                if ("String".equals(quotaDTO.getValueType()) ||
                        "Boolean".equals(quotaDTO.getValueType())){
                    quotaDTO.setValue(0d);
                    quotaDTO.setStringValue((String)payloadMap.get(quotaKey));
                }else {
                    //如果是数值
                    if (payloadMap.get(quotaKey) instanceof String){
                        // 转为数值
                        quotaDTO.setValue(Double.valueOf((String)payloadMap.get(quotaKey)));
                        // 转为String
                        quotaDTO.setStringValue((String)payloadMap.get(quotaKey));
                    }else {
                        // 转为数值
                        quotaDTO.setValue(Double.valueOf(payloadMap.get(quotaKey) + ""));
                        // 转为String
                        quotaDTO.setStringValue(quotaDTO.getValue() + "");
                    }

                    quotaDTO.setDeviceId( deviceId );
                }

                quotaDTOList.add(quotaDTO);
            }
        }

        //4.封装设备 + 指标列表返回
        DeviceInfoDTO deviceInfoDTO = new DeviceInfoDTO();
        deviceInfoDTO.setDevice(deviceDTO);
        deviceInfoDTO.setQuotaList(quotaDTOList);

        return deviceInfoDTO;
    }
}
