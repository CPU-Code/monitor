package com.cpucode.monitor.es;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author : cpucode
 * @date : 2021/10/1 16:14
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
@Slf4j
public class ESRepository {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加设备
     * @param deviceDTO
     */
    public void addDevices(DeviceDTO deviceDTO){
        // 校验
        if (deviceDTO == null){
            return;
        }
        if (deviceDTO.getDeviceId() == null){
            return;
        }

        // 添加索引设备
        IndexRequest request = new IndexRequest("devices");

        try {
            // 转换为json 再转Map
            String json = JsonUtil.serialize(deviceDTO);
            Map map = JsonUtil.getByJson(json, Map.class);
            // 值
            request.source(map);
            // id
            request.id(deviceDTO.getDeviceId());
            // 插入
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("设备添加发生异常");
        }
    }
}
