package com.cpucode.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 指标配置表
 *
 * @author : cpucode
 * @date : 2021/10/1 15:30
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@TableName(value = "tb_quota")
@Data
public class QuotaEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标单位
     */
    private String unit;

    /**
     * 报文主题
     */
    private String subject;

    /**
     * 指标值字段
     */
    private String valueKey;

    /**
     * 设备识别码字段(设备Id)
     */
    private String snKey;

    /**
     * web钩子地址
     */
    private String webhook;

    /**
     * 指标字段类型，Double、Inteter、Boolean
     */
    private String valueType;

    /**
     * 参考值
     */
    private String referenceValue;
}
