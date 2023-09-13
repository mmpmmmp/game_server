package com.wre.game.api;

import com.wre.game.api.entity.RechargeInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class test {
    @Resource
    private RechargeInfo serverInfo;

    @Test
    public void yml(){
        System.out.println(serverInfo.getPort());
    }

}
