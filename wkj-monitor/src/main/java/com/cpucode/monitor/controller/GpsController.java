package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.DeviceFullInfo;
import com.cpucode.monitor.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/5 13:47
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/gps")
public class GpsController {
    @Autowired
    private GpsService gpsService;

    /**
     * 根据经纬度获取一定范围内的设备信息
     * @param lat 纬度
     * @param lon 经度
     * @param distance 半径
     * @return
     */
    @GetMapping("/deviceList/{lat}/{lon}/{distance}")
    public List<DeviceFullInfo> getDeviceFullInfo(@PathVariable Double lat,
                                                  @PathVariable Double lon,
                                                  @PathVariable Integer distance){
        return gpsService.getDeviceFullInfo(lat, lon, distance);
    }
}
