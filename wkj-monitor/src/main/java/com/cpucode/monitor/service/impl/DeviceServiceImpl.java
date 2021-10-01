package com.cpucode.monitor.service.impl;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : cpucode
 * @date : 2021/10/1 17:10
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private ESRepository esRepository;

    /**
     * 更改设备状态
     * @param deviceId 设备id
     * @param status 设备状态
     * @return
     */
    @Override
    public boolean setStatus(String deviceId, Boolean status) {
        // 校验
        if (deviceId == null){
            return false;
        }
        if (status == null){
            return false;
        }

        // 查询数据是否存在
        DeviceDTO deviceDTO = findDevice(deviceId);
        if (deviceDTO == null){
            return false;
        }

        //更新数据
        return esRepository.updateStatus(deviceId, status);
    }


    /**
     * 查询设备
     * @param deviceId 设备id
     * @return
     */
    private DeviceDTO findDevice(String deviceId){
        // 根据设备id 查询数据 TODO: redis缓存查询
        DeviceDTO deviceDTO = esRepository.searchDeviceById(deviceId);

        return deviceDTO;
    }
}
