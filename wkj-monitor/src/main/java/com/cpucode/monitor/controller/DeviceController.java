package com.cpucode.monitor.controller;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.service.DeviceService;
import com.cpucode.monitor.service.NoticeService;
import com.cpucode.monitor.vo.DeviceQuotaVO;
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

    @Autowired
    private NoticeService noticeService;

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
    public void clientAction(@RequestBody Map<String, String> param){
        System.out.println(param);

        //提取设备id
        String deviceId = param.get("clientid");

        if (param.get("action").equals("client_connected")){
            //如果是联网
            deviceService.updateOnline(deviceId, true);
            //联网透传
            noticeService.onlineTransfer(deviceId, true);
        }
        if (param.get("action").equals("client_disconnected")){
            //如果是断网
            deviceService.updateOnline(deviceId, false);
            //断网透传
            noticeService.onlineTransfer(deviceId, false);
        }
    }

    /**
     * 查询设备详情
     * @param page 页数
     * @param pageSize 页码
     * @param deviceId 设备id
     * @param tag 标签
     * @param state 启用
     * @return
     */
    public Pager<DeviceQuotaVO> queryQuotaData(@RequestParam(value="page",required = false,defaultValue = "1")
                                                       Long page,
                                              @RequestParam(value = "pageSize",required = false,defaultValue = "10")
                                                      Long pageSize,
                                              @RequestParam(value = "deviceId",required = false)
                                                       String deviceId,
                                              @RequestParam(value = "tag",required = false)
                                                       String tag,
                                              @RequestParam(value = "state",required = false)
                                                       Integer state){
        return deviceService.queryDeviceQuota(page, pageSize, deviceId, tag, state);
    }


}
