package com.cpucode.monitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpucode.monitor.entity.QuotaEntity;
import com.cpucode.monitor.service.QuotaService;
import com.cpucode.monitor.vo.QuotaVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:28
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
@RequestMapping("/quote")
public class QuotaController {
    @Autowired
    private QuotaService quotaService;

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

        // 插入数据
        return quotaService.save(quotaEntity);
    }
}
