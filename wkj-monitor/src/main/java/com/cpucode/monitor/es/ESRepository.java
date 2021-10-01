package com.cpucode.monitor.es;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.util.JsonUtil;
import com.cpucode.monitor.vo.Pager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
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

    /**
     * 更新设备标签
     * @param deviceId 设备id
     * @param tags 设备标签
     * @return
     */
    public boolean updateDeviceTag(String deviceId, String tags){
        //更新条件
        UpdateRequest updateRequest = new UpdateRequest("devices", deviceId)
                .doc("tag", tags);

        try{
            // 更新操作
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return true;
        }catch (IOException e){
            e.printStackTrace();
            log.error("更新设备标签出错");

            return false;
        }
    }

    /**
     * 更新设备告警信息
     * @param deviceDTO
     * @return
     */
    public boolean updateDevicesAlarm(DeviceDTO deviceDTO){
        UpdateRequest updateRequest = new UpdateRequest("device", deviceDTO.getDeviceId())
                .doc(   "alarm", deviceDTO.getAlarm(),  //是否告警
                        "level", deviceDTO.getLevel(),  //告警级别
                        "alarmName", deviceDTO.getAlarmName());     //告警名称

        try {
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新设备告警信息出错");
            return false;
        }
    }

    /**
     * 更新在线状态
     * @param deviceId 设备id
     * @param online 在线状态
     * @return
     */
    public boolean updateOnline(String deviceId, Boolean online){
        UpdateRequest updateRequest = new UpdateRequest("devices",deviceId)
                .doc( "online",online );

        try {
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新在线状态出错");

            return false;
        }
    }

    /**
     * 分页查询设备
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param deviceId 设备编号
     * @param tags 标签
     * @param state 状态
     * @return
     */
    public Pager<DeviceDTO> searchDevice(Long page, Long pageSize,
                                         String deviceId, String tags,
                                         Integer state){
        SearchRequest searchRequest = new SearchRequest("devices");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //设备编号
        if(!Strings.isNullOrEmpty(deviceId)){
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("deviceId", deviceId + "*" ));
        }
        //标签
        if(!Strings.isNullOrEmpty(tags)){
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("tag", "*" + tags + "*" ));
        }

        // 状态（在线状态和告警状态）  0：在线  1：离线  2：一般告警  3：严重告警
        if(state != null){
            boolQueryBuilder.must(QueryBuilders.termQuery("status", true));

            //在线
            if(state.intValue() == 0){
                boolQueryBuilder.must(QueryBuilders.termQuery("online", true));
            }
            //离线
            if(state.intValue() == 1){
                boolQueryBuilder.must(QueryBuilders.termQuery("online", false));
            }

            //一般报警
            if(state.intValue() == 2){
                boolQueryBuilder.must(QueryBuilders.termQuery("level", 1));
            }
            //严重报警
            if(state.intValue() == 3){
                boolQueryBuilder.must(QueryBuilders.termQuery("level", 2));
            }
        }

        //分页 , 总页数
        sourceBuilder.from((page.intValue() - 1) * pageSize.intValue());
        // 页大小
        sourceBuilder.size(pageSize.intValue());
        //分页查询，可以得到全部的结果数
        sourceBuilder.trackTotalHits(true);

        //告警严重的排在前面
        sourceBuilder.sort("level", SortOrder.DESC);

        // 查询条件
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        try {
            // 查询集合
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 数据值集合
            SearchHits searchHits = searchResponse.getHits();

            // 创建一个list集合
            List<DeviceDTO> devices = Lists.newArrayList();

            for(SearchHit hit : searchHits){
                //转为 string
                String hitResult = hit.getSourceAsString();
                // json 转 类
                DeviceDTO deviceDTO = JsonUtil.getByJson(hitResult, DeviceDTO.class);

                // 数据添加到 list 中
                devices.add(deviceDTO);
            }

            // 总数
            Long count = searchResponse.getHits().getTotalHits().value;

            Pager<DeviceDTO> pager = new Pager<>(count, pageSize);
            // 数据
            pager.setItems(devices);

            return pager;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("查询设备异常");

            return null;
        }
    }
}
