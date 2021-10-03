package com.cpucode.monitor.influx;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author : cpucode
 * @date : 2021/10/3 16:31
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
@Slf4j
public class InfluxRepository {
    @Autowired
    private InfluxDB influxDB;

    @Value("${spring.influx.db}")
    private String dbName;

    /**
     * 添加数据
     * @param object
     */
    public void add( Object object ){

        Point.Builder builder = Point.measurementByPOJO(object.getClass());

        // 所有属性转换为tag添加到point中
        // 调用time方法将 UTC时间转换为北京时间 设置进去
        Point point = builder
                .addFieldsFromPOJO(object)
                .time(LocalDateTime.now()
                        .plusHours(8)
                        .toInstant(ZoneOffset.of("+8"))
                        .toEpochMilli(), TimeUnit.MILLISECONDS)
                .build();

        // 存储的数据库名称
        influxDB.setDatabase(dbName);
        // 插入到表(Measurement)中
        influxDB.write(point);

        influxDB.close();
    }
}
