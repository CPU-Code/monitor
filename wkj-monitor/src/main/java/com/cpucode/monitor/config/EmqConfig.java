package com.cpucode.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : cpucode
 * @date : 2021/10/2 11:30
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
@Configuration
@ConfigurationProperties("emq")
public class EmqConfig {
    /**
     * 服务端连接地址
     */
    private String mqttServerUrl;
}
