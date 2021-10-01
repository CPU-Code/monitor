package com.cpucode.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpucode.monitor.entity.AdminEntity;
import com.cpucode.monitor.mapper.AdminMapper;
import com.cpucode.monitor.service.AdminService;
import com.google.common.base.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author : cpucode
 * @date : 2021/10/1 13:15
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, AdminEntity> implements AdminService {

    /**
     * 登录
     * @param loginName 用户名
     * @param password 密码
     * @return
     */
    @Override
    public Integer login(String loginName, String password) {
        // 判断是否为空 或 null
        if (Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(password)){
            return -1;
        }

        // 查询是否有该用户
        QueryWrapper<AdminEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AdminEntity::getLoginName, loginName);
        AdminEntity adminEntity = this.getOne(queryWrapper);

        if (adminEntity == null){
            return -1;
        }

        // SHA-256 + 随机盐 + 密钥对密码进行加密
        // matches 进行的比较 加密后的值
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, adminEntity.getPassword())){
            return adminEntity.getId();
        }

        return -1;
    }
}
