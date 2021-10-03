package com.cpucode.monitor.service;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.vo.DeviceQuotaVO;
import com.cpucode.monitor.vo.Pager;

/**
 * @author : cpucode
 * @date : 2021/10/1 17:10
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface DeviceService {
    /**
     * 更改设备状态
     * @param deviceId 设备id
     * @param status 设备状态
     * @return
     */
    boolean setStatus(String deviceId, Boolean status);

    /**
     * 更新设备标签
     * @param deviceId 设备id
     * @param tags 设备标签
     * @return
     */
    boolean updateTags(String deviceId,String tags);

    /**
     * 搜索设备
     * @param page
     * @param pageSize
     * @param sn
     * @param tag
     * @return
     */
    Pager<DeviceDTO> queryPage(Long page, Long pageSize, String sn, String tag, Integer status);

    /**
     * 存储设备信息
     * @param deviceDTO  设备信息
     * @return
     */
    boolean saveDeviceInfo(DeviceDTO deviceDTO);

    /**
     * 更新在线状态
     * @param deviceId 设备id
     * @param online 是否在线
     */
    void updateOnline(String deviceId, Boolean online);

    /**
     * 查询设备详情
     * @param page 页数
     * @param pageSize 页码
     * @param deviceId 设备id
     * @param tag 标签
     * @param state 启用
     * @return
     */
    Pager<DeviceQuotaVO> queryDeviceQuota(Long page, Long pageSize, String deviceId, String tag, Integer state);
}
