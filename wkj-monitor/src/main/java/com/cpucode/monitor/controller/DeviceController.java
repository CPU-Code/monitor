package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.service.DeviceService;
import com.cpucode.monitor.vo.DeviceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cpucode
 * @date : 2021/10/1 17:17
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 更改设备状态
     * @param deviceVO 设备视图
     * @return
     */
    @PutMapping("/status")
    public boolean setStatus(@RequestBody DeviceVO deviceVO){
        return deviceService.setStatus(deviceVO.getSn(), deviceVO.getStatus());
    }

    /**
     * 设置设备标签
     * @param deviceVO
     * @return
     */
    @PutMapping("/tags")
    public boolean setTags(@RequestBody DeviceVO deviceVO){
        return deviceService.updateTags(deviceVO.getSn(), deviceVO.getTags());
    }
}
