package com.cpucode.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpucode.monitor.config.MybatisRedisCache;
import com.cpucode.monitor.entity.AdminEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : cpucode
 * @date : 2021/10/1 13:14
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Mapper
@CacheNamespace(implementation= MybatisRedisCache.class,eviction= MybatisRedisCache.class)
public interface AdminMapper extends BaseMapper<AdminEntity> {
}
