package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户视图
 *
 * @author : cpucode
 * @date : 2021/10/1 13:45
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class AdminVO implements Serializable {
    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;
}
