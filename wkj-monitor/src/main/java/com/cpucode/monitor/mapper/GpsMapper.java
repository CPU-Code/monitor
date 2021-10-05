package com.cpucode.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpucode.monitor.config.MybatisRedisCache;
import com.cpucode.monitor.entity.GPSEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : cpucode
 * @date : 2021/10/5 9:54
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Mapper
@CacheNamespace(implementation= MybatisRedisCache.class,eviction= MybatisRedisCache.class)
public interface GpsMapper extends BaseMapper<GPSEntity> {
}
