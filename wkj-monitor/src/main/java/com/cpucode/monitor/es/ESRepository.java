package com.cpucode.monitor.es;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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

    /**
     * 查询设备
     * @param deviceId 设备id
     * @return
     */
    public DeviceDTO searchDeviceById(String deviceId){
        // 创建查询
        SearchRequest searchRequest = new SearchRequest("devices");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询条件
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", deviceId));
        searchRequest.source(searchSourceBuilder);

        try {
            // 查询结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 查询json 值
            SearchHits hits = searchResponse.getHits();
            // 数据和
            long hitsCount = hits.getTotalHits().value;

            // 校验
            if(hitsCount<=0) return null;

            DeviceDTO deviceDTO = null;

            for(SearchHit hit : hits){
                // 转成 String
                String hitResult = hit.getSourceAsString();
                // json 转 类
                deviceDTO = JsonUtil.getByJson(hitResult, DeviceDTO.class);

                // 设置设备id
                deviceDTO.setDeviceId(deviceId);

                // TODO
                break;
            }

            // 返回数据
            return deviceDTO;

        } catch (IOException e) {
            e.printStackTrace();
            log.error("查询设备异常");

            return null;
        }
    }

    /**
     * 更新设备状态
     * @param deviceId 设备id
     * @param status 设备状态
     * @return
     */
    public boolean updateStatus(String deviceId, Boolean status){
        // 跟新条件
        UpdateRequest updateRequest = new UpdateRequest("devices", deviceId)
                .doc("status", status);

        try{
            // 跟新操作
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return true;
        }catch (IOException e) {
            e.printStackTrace();
            log.error("更新设备状态出错");
            return false;
        }
    }
}
