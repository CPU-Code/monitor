package com.cpucode.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : cpucode
 * @date : 2021/10/1 10:29
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
//@EnableDiscoveryClient
//@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableAsync
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run( MonitorApplication.class, args);
    }
}
