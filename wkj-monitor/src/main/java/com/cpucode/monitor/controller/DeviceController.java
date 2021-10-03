package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.service.DeviceService;
import com.cpucode.monitor.vo.DeviceVO;
import com.cpucode.monitor.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    /**
     * 分页搜索设备
     *
     * @param page  页码
     * @param pageSize  页大小
     * @param sn  设备id
     * @param tag  标签
     * @return
     */
    @GetMapping
    public Pager<DeviceDTO> findPage(@RequestParam(value = "page", required = false, defaultValue = "1")
                                                 Long page,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                             Long pageSize,
                                     @RequestParam(value = "sn", required = false)
                                                 String sn,
                                     @RequestParam(value = "tag", required = false)
                                                 String tag){
        return deviceService.queryPage(page, pageSize, sn, tag,null);
    }

    /**
     * 接收设备断连信息
     * @param param
     */
    @PostMapping("/clientAction")
    public void clientAction(@RequestBody Map<String,String> param){
        System.out.println(param);
    }
}
