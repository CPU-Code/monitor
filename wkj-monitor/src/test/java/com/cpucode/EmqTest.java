package com.cpucode;

import com.cpucode.monitor.MonitorApplication;
import com.cpucode.monitor.emq.EmqClient;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : cpucode
 * @date : 2021/10/2 11:44
 * @github : https://github.com/CPU-Code
 * @csdn : https://blog.csdn.net/qq_44226094
 */
@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class EmqTest {
    @Autowired
    private EmqClient emqClient;

    /**
     * 测试连接和发送
     */
    @Test
    public void testSend(){
        emqClient.connect();

        emqClient.publish("test_topic","test_content");
    }
}
