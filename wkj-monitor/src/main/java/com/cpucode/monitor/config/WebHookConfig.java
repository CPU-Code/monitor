package com.cpucode.monitor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : cpucode
 * @date : 2021/10/5 21:16
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */

@Configuration
@ConfigurationProperties("webhook")
@Data
public class WebHookConfig {
    /**
     * 断连透传
     */
    private String online;
}
