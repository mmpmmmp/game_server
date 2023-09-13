package com.wre.game.api;

import com.wre.game.api.component.DataComponent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class DataComponentTest {
    @Autowired
    DataComponent dataComponent;


    {
        System.setProperty("spring.profiles.active", "local");
    }

    @Test
    public void testInsertData() {
        String uuid = "my_uuid";
        String appId = "wxcf0f81b4d40666c4";
        String data = "jason text3";
        dataComponent.setGameData(uuid, appId, data);

        String dataGetFromRedis = dataComponent.getGameData(uuid, appId);

        Assert.assertEquals(data, dataGetFromRedis);
    }

    @Test
    public void testSaveGameConfig() {
        String data = "{\"hello\":\"name\"}";
        dataComponent.setGameConfigJson(data);

        String dataGetFromRedis = dataComponent.getGameConfigJson();

        Assert.assertEquals(data, dataGetFromRedis);
    }

}
