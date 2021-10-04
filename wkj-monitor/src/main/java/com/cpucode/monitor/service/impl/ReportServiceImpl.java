package com.cpucode.monitor.service.impl;

import com.cpucode.monitor.dto.*;
import com.cpucode.monitor.es.ESRepository;
import com.cpucode.monitor.influx.InfluxRepository;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.vo.Pager;
import com.cpucode.monitor.vo.PieVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : cpucode
 * @date : 2021/10/4 16:00
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ESRepository esRepository;

    @Autowired
    private InfluxRepository influxRepository;

    /**
     * 设备状态分布
     * @return
     */
    @Override
    public List<PieVO> getStatusCollect() {
        //全部设备数量
        Long allDeviceCount = esRepository.getAllDeviceCount();
        //离线设备数量
        Long offlineCount = esRepository.getOfflineCount();
        //报警设备数量
        Long alarmCount = esRepository.getAlarmCount();

        PieVO devicePie = new PieVO();
        devicePie.setName("正常运转");
        devicePie.setValue(allDeviceCount - offlineCount - alarmCount);

        PieVO offlinePie = new PieVO();
        offlinePie.setName("离线");
        offlinePie.setValue(offlineCount);

        PieVO alarmPie = new PieVO();
        offlinePie.setName("警告");
        offlinePie.setValue(alarmCount);

        List<PieVO> pieVOList= Lists.newArrayList();
        pieVOList.add(devicePie);
        pieVOList.add(offlinePie);
        pieVOList.add(alarmPie);

        return pieVOList;
    }

    /**
     * 获取异常趋势指标
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param type 时间统计类型(1:60分钟之内, 2:当天24小时, 3:7天内)
     * @return
     */
    @Override
    public List<TrendPoint> getAlarmTrend(String start, String end, int type){
        StringBuilder sql = new StringBuilder("select count(value) as pointValue " +
                "from quota " +
                "where alarm = '1");

        sql.append("and time >= '" + start + "' and time <= '" + end + "'");

        if (type == 1){
            sql.append("group by time(1m)");
        }else if (type == 2){
            sql.append("group by time(1h)");
        }else if (type == 3){
            sql.append("group by time(1d)");
        }

        List<TrendPoint> trendPointList =
                influxRepository.query(sql.toString(), TrendPoint.class);

        return trendPointList;
    }

    /**
     * 获取一定时间范围之内的报警次数最多的设备指标
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public List<HeapPoint> getTop10Alarm(String startTime, String endTime){
        StringBuilder sql =
                new StringBuilder("select top(heapValue, deviceId, quotaId, quotaName, 10) as heapValue" +
                        "from(select count(value) as heapValue " +
                        "from quota where alarm = '1'");

        sql.append("and time >= '");
        sql.append(startTime);
        sql.append("' and time <= '");
        sql.append(endTime);
        sql.append("' group by deviceId, quotaId) order by desc");

        return influxRepository.query(sql.toString(), HeapPoint.class);
    }

    /**
     * 通过指标获取关联设备
     * @param page 页数
     * @param pageSize 页码
     * @param quotaId 指标id
     * @return
     */
    @Override
    public Pager<String> getDeviceByQuota(Long page, Long pageSize, String quotaId){
        String fromQl = " from ( select deviceId, value " +
                "from quota " +
                "where quotaId = '" + quotaId + "' " +
                "group by deviceId, quotaId)";
        String listQl = "select distinct(devicesId) as deviceId " + fromQl +
                "limit" + pageSize + "offset" + (page - 1) * pageSize;

        String countQl = "select count(distinct(deviceId)) as count" + fromQl;

        List<QuotaInfo> quotaInfoList = influxRepository.query(listQl, QuotaInfo.class);

        //设备id列表
        List<String> deviceIdList = quotaInfoList.stream().map(quotaInfo ->
            quotaInfo.getDeviceId()
        ).collect(Collectors.toList());

        //统计记录个数
        List<QuotaCount> quotaCountList =
                influxRepository.query(countQl, QuotaCount.class);

        // 校验空值
        if( quotaCountList == null || quotaCountList.size() == 0 ){
            Pager<String> pager = new Pager<String>(0L, 0L);
            pager.setItems(Lists.newArrayList());

            return pager;
        }

        Long count = quotaCountList.get(0).getCount();

        Pager<String> pager = new Pager<String>(count, pageSize);
        pager.setItems(deviceIdList);
        return pager;
    }

    /**
     * 获取指标趋势
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param quotaId 指标Id
     * @param type 时间统计类型(1:60分钟之内,2:当天24小时,3:7天内)
     * @param deviceId 设备编码
     * @return
     */
    @Override
    public List<TrendPoint2> getQuotaTrend(String start, String end, String quotaId, String deviceId, int type){
        StringBuilder ql = new StringBuilder("select first(value) as pointValue from quota");
        ql.append("where time >= '" + start +"' and time <= '" + end + "'");
        ql.append("and quotaId = '" + quotaId + "'");
        ql.append("and deviceId = '" + deviceId + "'");

        if (type == 1){
            //1小时
            ql.append("group by time(1m)");
        } else if (type == 2){
            //1天
            ql.append("group by time(1h)");
        }else if (type == 3){
            //7天
            ql.append("group by time(1d)");
        }

        List<TrendPoint2> trendPoint2List = influxRepository.query(ql.toString(), TrendPoint2.class);
        return trendPoint2List;
    }
}
