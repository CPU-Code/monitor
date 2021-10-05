package com.cpucode.monitor.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author : cpucode
 * @date : 2021/10/5 20:04
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Slf4j
public class HttpUtil {
    public static void httpPost(String url, Object msg){
        if(Strings.isNullOrEmpty(url)) {
            return;
        }

        new Thread(()->{
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            try {
                restTemplate.postForObject(url, msg, String.class);
            }catch (Exception e){
                log.error("post alarm msg error", e);
            }
        }).start();
    }
}
