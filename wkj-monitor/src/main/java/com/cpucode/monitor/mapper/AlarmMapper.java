package com.cpucode.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpucode.monitor.config.MybatisRedisCache;
import com.cpucode.monitor.entity.AlarmEntity;
import org.apache.ibatis.annotations.*;

/**
 * @author : cpucode
 * @date : 2021/10/2 20:52
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */

@CacheNamespace(implementation= MybatisRedisCache.class, eviction= MybatisRedisCache.class)
@Mapper
public interface AlarmMapper extends BaseMapper<AlarmEntity> {

    @Results(id="alarmMap",value = {
            @Result(property = "quota",column = "quota_id", one = @One(select = "com.cpucode.monitor.mapper.QuotaMapper.selectById")),
            @Result(property = "quotaId",column = "quota_id")
    })
    @Select("select * from tb_alarm where id=#{id}")
    Page<AlarmEntity> queryPage(Page<AlarmEntity> page, Integer id);
}
