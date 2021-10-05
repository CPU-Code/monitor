package com.cpucode.monitor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * gps表结构
 *
 * @author : cpucode
 * @date : 2021/10/5 9:46
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@TableName(value = "tb_gps")
@Data
public class GPSEntity implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    /**
     * 主题
     */
    private String subject;
    /**
     * 设备识别码
     */
    private String snKey;

    /**
     * 是否是单字段
     */
    private Boolean singleField;

    /**
     * 经纬度字段
     */
    private String valueKey;

    /**
     * 经纬度分隔符
     */
    private String separation;

    /**
     * 经度字段
     */
    private String longitude;

    /**
     * 纬度字段
     */
    private String latitude;
}
