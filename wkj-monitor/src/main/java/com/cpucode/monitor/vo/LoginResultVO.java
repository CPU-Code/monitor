package com.cpucode.monitor.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : cpucode
 * @date : 2021/10/1 13:44
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Data
public class LoginResultVO implements Serializable {
    /**
     * 登录结果
     */
    private Boolean loginSuccess;

    /**
     * 管理员Id
     */
    private Integer adminId;

    /**
     * jwt token
     */
    private String token;
}
