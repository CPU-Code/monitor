package com.cpucode.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpucode.monitor.entity.AdminEntity;

/**
 * @author : cpucode
 * @date : 2021/10/1 13:15
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
public interface AdminService extends IService<AdminEntity> {
    /**
     * 登录
     * @param loginName 用户名
     * @param password 密码
     * @return
     */
    Integer login(String loginName, String password);

    /**
     * 注册
     * @param loginName 用户名
     * @param password 密码
     * @return
     */
    Integer register(String loginName, String password);
}
