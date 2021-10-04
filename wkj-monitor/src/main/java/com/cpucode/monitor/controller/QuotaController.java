package com.cpucode.monitor.controller;

import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.entity.QuotaEntity;
import com.cpucode.monitor.service.QuotaService;
import com.cpucode.monitor.vo.Pager;
import com.cpucode.monitor.vo.QuotaVO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:28
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/quote")
@Slf4j
public class QuotaController {
    @Autowired
    private QuotaService quotaService;

    @Autowired
    private EmqClient emqClient;

    /**
     * 创建指标
     *
     * @param vo
     * @return
     */
    @PostMapping
    public boolean create(@RequestBody QuotaVO vo){
        QuotaEntity quotaEntity = new QuotaEntity();
        // 对象数据的拷贝
        BeanUtils.copyProperties(vo, quotaEntity);

        try {
            // 订阅 创建的主题
            emqClient.subscribe("$queue/" + quotaEntity.getSubject());
        } catch (MqttException e) {
            log.error("订阅主题失败", e);
            return false;
        }

        // 插入数据
        return quotaService.save(quotaEntity);
    }

    /**
     * 分页获取数值型指标
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/numberQuota")
    public Pager<QuotaEntity> queryNumberQuota(@RequestParam(value = "page", required = false, defaultValue = "1")
                                                           Long page,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                                       Long pageSize){
        return new Pager<>(quotaService.queryNumberQuota(page, pageSize));
    }
}
