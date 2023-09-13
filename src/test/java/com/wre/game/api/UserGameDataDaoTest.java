package com.wre.game.api;

import com.wre.game.api.dao.UserGameDataDao;
import com.wre.game.api.dao.UserGameDataStorageDao;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import com.wre.game.api.data.entity.UserGameDataStorage;
import com.wre.game.api.service.UserDataStorageService;
import com.wre.game.api.service.impl.UserDataStorageServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserGameDataDaoTest {
    @Autowired
    private UserGameDataDao userGameDataDao;

    {
        System.setProperty("spring.profiles.active", "local");
    }

    /**
     * test insert
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        UserGameData ugc = new UserGameData();
        ugc.setAppId("app_id");
        ugc.setAppName("app_name");
        ugc.setCreatedAt(new Date());
        ugc.setParam1("1");
        ugc.setParam2("1");
        ugc.setParam3("1");
        ugc.setParam4("1");
        ugc.setParam5("1");
        ugc.setUuid("22a8e22ba2d24d479b5908c30dc48896");
        userGameDataDao.insertUserGameData(ugc);

    }

    @Test
    public void testUpdate() throws Exception {
        UserGameData ugc = userGameDataDao.selectUserGameData("c1e75d06403e48f083ee542290d8a346", "app_id");
        ugc.setAppName("app_name2");
        ugc.setParam1("1");
        ugc.setParam2("1");
        ugc.setParam3("1");
        ugc.setParam4("1");
        ugc.setParam5("1");
        ugc.setScore(13);
        ugc.setUuid("c1e75d06403e48f083ee542290d8a346");
        userGameDataDao.updateUserGameData(ugc);
    }

    @Test
    public void testRank() throws Exception {
        List<UserRankDTO> list = userGameDataDao.selectGameDataRank("app_id", 100);
        for (UserRankDTO u :
                list) {
            System.out.println(u);
        }
    }
    @Test
    public void  testGetGameDataByUserId(){
        String userId = "38041334";
        String appId  ="19623571";
        UserDataStorageService service = new UserDataStorageServiceImpl();
       // UserGameDataStorageDao service =
        UserGameDataStorage userData = service.getUserGameDataStorageByUserId(userId,appId);
        System.out.println(userData.toString());

    }
}