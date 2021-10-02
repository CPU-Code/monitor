package com.cpucode.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 报警配置表
 *
 * @author : cpucode
 * @date : 2021/10/2 18:04
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@TableName(value = "tb_alarm", resultMap = "alarmMap", autoResultMap = true)
@Data
public class AlarmEntity implements Serializable {
    /**
     * 自增主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 报警名称
     */
    private String name;

    /**
     * 指标id
     */
    private Integer quotaId;

    /**
     * 运算符
     */
    private String operator;

    /**
     * 报警阈值
     */
    private Integer threshold;

    /**
     * 报警级别 1一般 2严重
     */
    private Integer level;

    /**
     * 沉默周期（分钟）
     */
    private Integer cycle;

    /**
     * web钩子
     */
    @TableField(value = "webhook")
    private String webHook;

    /**
     * 指标配置表
     */
    @TableField(exist = false)
    private QuotaEntity quota;
}
