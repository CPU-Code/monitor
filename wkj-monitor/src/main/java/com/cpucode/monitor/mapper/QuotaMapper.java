package com.cpucode.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpucode.monitor.entity.QuotaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author : cpucode
 * @date : 2021/10/1 15:37
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Mapper
public interface QuotaMapper extends BaseMapper<QuotaEntity> {
    /**
     * 根据主题查询指标配置列表
     * @param subject
     * @return
     */
    @Select("select *" +
            "from tb_quota" +
            "where subject = #{subject}")
    List<QuotaEntity> selectBySubject(String subject);
}
