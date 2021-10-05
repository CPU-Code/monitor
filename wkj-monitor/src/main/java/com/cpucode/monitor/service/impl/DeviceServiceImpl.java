package com.cpucode.monitor.service.impl;

import com.cpucode.common.SystemDefinition;
import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.dto.QuotaInfo;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.service.DeviceService;
import com.cpucode.monitor.service.QuotaService;
import com.cpucode.monitor.vo.DeviceQuotaVO;
import com.cpucode.monitor.vo.Pager;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private QuotaService quotaService;

    @Autowired
    private RedisTemplate redisTemplate;

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

        boolean b = esRepository.updateStatus(deviceId, status);

        //刷新到缓存
        deviceDTO.setStatus(status);
        refreshDevice(deviceDTO);

        //更新数据
        return b;
    }


    /**
     * 更新设备标签
     * @param deviceId 设备id
     * @param tags 设备标签
     * @return
     */
    @Override
    public boolean updateTags(String deviceId, String tags){
        // 校验
        if (deviceId == null){
            return false;
        }
        if (tags == null){
            return false;
        }

        DeviceDTO deviceDTO = findDevice(deviceId);
        if (deviceDTO == null){
            return false;
        }

        return esRepository.updateDeviceTag(deviceId, tags);
    }

    /**
     * 分页搜索设备
     * @param page 页码
     * @param pageSize 页大小
     * @param sn    设备id
     * @param tag  标签
     * @param status  状态
     * @return
     */
    @Override
    public Pager<DeviceDTO> queryPage(Long page, Long pageSize,
                                      String sn, String tag,
                                      Integer status) {
        return esRepository.searchDevice(page, pageSize, sn, tag, status);
    }

    /**
     * 存储设备信息
     * @param deviceDTO  设备信息
     * @return
     */
    @Override
    public boolean saveDeviceInfo(DeviceDTO deviceDTO) {
        //查询设备 ，判断开关状态 ，如果是关闭则不处理
        DeviceDTO device = findDevice(deviceDTO.getDeviceId());
        if (device != null && !device.getStatus()){
            return false;
        }

        // 如果当前设备查不到，新增
        if (device == null){
            esRepository.addDevices(deviceDTO);
        }else {
            //如果可以查询到，更新告警信息
            esRepository.updateDevicesAlarm(deviceDTO);
        }

        //刷新到缓存
        refreshDevice(deviceDTO);

        return true;
    }

    /**
     * 更新
     * @param deviceId 设备id
     * @param online 是否在线
     */
    @Override
    public void updateOnline(String deviceId, Boolean online){
        //以webclient开头的client为系统前端, monitor开头的是亿可控服务端
        if(deviceId.startsWith("webclient") ||
                deviceId.startsWith("monitor")){
            return;
        }

        //更新数据到es
        DeviceDTO deviceDTO = findDevice(deviceId);
        if(deviceDTO == null) {
            return;
        }

        esRepository.updateOnline(deviceId, online);

        //刷新到缓存
        deviceDTO.setOnline(online);
        refreshDevice(deviceDTO);
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
    @Override
    public Pager<DeviceQuotaVO> queryDeviceQuota(Long page, Long pageSize, String deviceId, String tag, Integer state){
        //1.查询设备列表
        Pager<DeviceDTO> pager = esRepository.searchDevice(page, pageSize, deviceId, tag, state);

        //2.查询指标列表
        List<DeviceQuotaVO> deviceQuotaVOList = Lists.newArrayList();

        pager.getItems().forEach(deviceDTO -> {
            DeviceQuotaVO deviceQuotaVO = new DeviceQuotaVO();
            BeanUtils.copyProperties(deviceDTO, deviceQuotaVO);
            //查询指标
            List<QuotaInfo> quotaList = quotaService.getLastQuotaList(deviceDTO.getDeviceId());
            deviceQuotaVO.setQuotaList(quotaList);

            deviceQuotaVOList.add(deviceQuotaVO);
        });

        //3.封装返回结果
        Pager<DeviceQuotaVO> pageResult = new Pager(pager.getCounts(), pageSize);
        pageResult.setItems(deviceQuotaVOList);

        return pageResult;
    }

    /**
     * 查询设备
     * @param deviceId 设备id
     * @return
     */
    private DeviceDTO findDevice(String deviceId){
        // 根据设备id 查询数据 TODO: redis缓存查询

        DeviceDTO deviceDTO = (DeviceDTO)redisTemplate.boundHashOps(SystemDefinition.DEVICE_KEY).get(deviceId);

        if(deviceDTO == null){
            deviceDTO = esRepository.searchDeviceById(deviceId);
            refreshDevice(deviceDTO);
        }

        return deviceDTO;
    }

    /**
     * 刷新缓存
     * @param deviceDTO
     */
    private void refreshDevice(DeviceDTO deviceDTO) {
        if(deviceDTO == null) {
            return;
        }

        redisTemplate.boundHashOps(SystemDefinition.DEVICE_KEY)
                .put(deviceDTO.getDeviceId(), deviceDTO);
    }
}
