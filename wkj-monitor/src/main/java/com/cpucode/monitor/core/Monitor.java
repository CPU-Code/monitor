package com.cpucode.monitor.core;

import com.cpucode.monitor.emq.EmqClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 自动监控
 *
 * @author : cpucode
 * @date : 2021/10/2 14:26
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Component
@Slf4j
public class Monitor {
    @Autowired
    private EmqClient emqClient;

    @PostConstruct
    public void init(){
        emqClient.connect();

        emqClient.subscribe("myTopic");
    }
}
