package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.entity.QuotaEntity;
import com.cpucode.monitor.mapper.QuotaMapper;
import com.cpucode.monitor.service.QuotaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:39
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class QuotaServiceImpl extends ServiceImpl<QuotaMapper, QuotaEntity> implements QuotaService {
    /**
     * 获取所有报文主题
     * @return 报文主题集合
     */
    @Override
    public List<String> getAllSubject() {
        QueryWrapper<QuotaEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(QuotaEntity::getSubject);

        return this.list(wrapper).stream()
                .map(q -> q.getSubject()).collect(Collectors.toList());
    }
}
