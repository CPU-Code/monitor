package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.dto.TrendPoint;
import com.cpucode.monitor.dto.TrendPoint2;
import com.cpucode.monitor.service.ReportService;
import com.cpucode.monitor.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/4 16:53
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class ReportTest {
    @Autowired
    private ReportService reportService;

    /**
     * 测试异常趋势指标
     */
    @Test
    public void testAlarmTrend(){
        List<TrendPoint> trendPointList =
                reportService.getAlarmTrend("2021-10-01", "2021-10-31", 3);

        for (TrendPoint trendPoint :trendPointList){
            try{
                System.out.println(JsonUtil.serialize(trendPoint));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询折线图类
     */
    @Test
    public void testQuotaTrend(){
        List<TrendPoint2> trendPoint2List =
                reportService.getQuotaTrend("2020-09-01 00:00:00", "2020-09-30 23:59:59", "1", "100001", 3);

        for( TrendPoint2 trendPoint2 : trendPoint2List ){
            try {
                System.out.println( JsonUtil.serialize(trendPoint2) );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
