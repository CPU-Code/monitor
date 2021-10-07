package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.emq.EmqClient;
import com.cpucode.monitor.service.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : cpucode
 * @date : 2021/10/7 10:29
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class AdminTest {
    @Autowired
    private AdminService adminService;

    @Test
    public void registerTest(){
        adminService.register("admin", "cpucode");
    }
}
