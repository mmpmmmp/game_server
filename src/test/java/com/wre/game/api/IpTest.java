package com.wre.game.api;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.adapter.ToutiaoAdapter;
import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.config.IpConfig;
import com.wre.game.api.data.*;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.util.GameUtils;
import com.wre.game.api.util.IpUtil;
import com.wre.game.api.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class IpTest {

    @Autowired
    IpConfig ipConfig;

    {
        System.setProperty("spring.profiles.active", "local");
    }

    @Test
    public void testIpBlock() {
        IpInfos ipInfos = ipConfig.ipInfos();
        System.out.println(ipInfos);

        String remoteIps = "49.246.224.0, 100.121.149.11";
        String ip = remoteIps;
        if (remoteIps != null) {
            String[] ipArr = StringUtils.split(remoteIps, ",");
            if (ipArr != null && ipArr.length > 0) {
                ip = ipArr[0];
            }
        }
        IpBlock ipBlock = IpUtil.checkIpBlock(ip, ipInfos.getBlockIpList());
        System.out.println(ipBlock);
    }
}