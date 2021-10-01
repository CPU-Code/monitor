package com.cpucode.monitor.controller;

import com.cpucode.monitor.service.AdminService;
import com.cpucode.monitor.util.JwtUtil;
import com.cpucode.monitor.vo.AdminVO;
import com.cpucode.monitor.vo.LoginResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : cpucode
 * @date : 2021/10/1 13:42
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 用户登录
     * @param admin
     * @return
     */
    @PostMapping("/login")
    public LoginResultVO login(@RequestBody AdminVO admin){
        LoginResultVO result = new LoginResultVO();
        Integer adminId = adminService.login(admin.getLoginName(), admin.getPasswrod());

        if (adminId < 0){
            result.setLoginSuccess(false);
            return result;
        }

        result.setAdminId(adminId);
        String token = JwtUtil.createJWT(adminId);
        result.setToken(token);
        result.setLoginSuccess(true);

        return result;
    }
}
