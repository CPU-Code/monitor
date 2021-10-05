package com.cpucode.monitor.es;

import com.cpucode.monitor.dto.DeviceDTO;
import com.cpucode.monitor.dto.DeviceLocation;
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
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
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
        UpdateRequest updateRequest = new UpdateRequest("devices", deviceId)
                .doc( "online", online);

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

    /**
     * 统计所有设备数量
     * @return
     */
    public Long getAllDeviceCount(){
        CountRequest countRequest = new CountRequest("devices");
        countRequest.query(QueryBuilders.matchAllQuery());

        try{
            CountResponse response = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            return response.getCount();
        } catch (IOException e){
            e.printStackTrace();

            return 0L;
        }
    }

    /**
     * 统计所有离线设备数量
     * @return
     */
    public Long getOfflineCount(){
        CountRequest countRequest = new CountRequest("devices");
        // 条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("online", false));

        countRequest.query(boolQueryBuilder);

        try{
            CountResponse response = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);

            return response.getCount();
        }catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 统计所有告警设备数量
     * @return
     */
    public Long getAlarmCount(){
        CountRequest countRequest = new CountRequest("devices");
        // 条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("online", true));
        boolQueryBuilder.must(QueryBuilders.termQuery("alarm", true));

        countRequest.query(boolQueryBuilder);

        try{
            // 查询
            CountResponse response = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);

            return response.getCount();
        } catch (IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 保存设备gps信息
     * @param deviceLocation
     */
    public void saveLocation(DeviceLocation deviceLocation){
        try{
            IndexRequest request = new IndexRequest("gps");
            request.source("location", deviceLocation.getLocation());
            request.id(deviceLocation.getDeviceId());

            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        }catch (Exception e){
            log.error("update es error",e);
        }
    }

    /**
     * 搜索一定距离之内的设备
     * @param lat 纬度
     * @param lon 经度
     * @param distance  距离坐标点半径
     * @return
     */
    public List<DeviceLocation> searchDeviceLocation(Double lat, Double lon, Integer distance){
        SearchRequest searchRequest = new SearchRequest("gps");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //中心点及半径构建
        GeoDistanceQueryBuilder geoDistanceQueryBuilder = new GeoDistanceQueryBuilder("location");
        geoDistanceQueryBuilder.distance(distance, DistanceUnit.KILOMETERS);
        geoDistanceQueryBuilder.point(lat, lon);
        searchSourceBuilder.query(geoDistanceQueryBuilder);

        //从近到远排序规则构建
        GeoDistanceSortBuilder distanceSortBuilder = new GeoDistanceSortBuilder("location", lat, lon);
        distanceSortBuilder.unit(DistanceUnit.KILOMETERS);
        //SortOrder.ASC 升序（由近到远)
        distanceSortBuilder.order(SortOrder.ASC);
        //GeoDistance.ARC  精准度高，计算较慢
        distanceSortBuilder.geoDistance(GeoDistance.ARC);
        searchSourceBuilder.sort(distanceSortBuilder);

        //只取前200个
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(200);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse =
                    restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            // 获取总数
            if(hits.getTotalHits().value <= 0){
                return Lists.newArrayList();
            }

            List<DeviceLocation> deviceLocationList = Lists.newArrayList();

            Arrays.stream(hits.getHits()).forEach(h->{
                DeviceLocation deviceLocation = new DeviceLocation();
                // 设备信息
                deviceLocation.setDeviceId(h.getId());
                // 位置信息
                deviceLocation.setLocation(h.getSourceAsMap().get("location").toString());

                deviceLocationList.add(deviceLocation);
            });

            return deviceLocationList;
        }catch (IOException e) {
            log.error("search location error",e);

            return Lists.newArrayList();
        }
    }
}
