package com.wre.game.api;

import com.wre.game.api.dao.UserActionAdDao;
import com.wre.game.api.dao.UserActionGameDao;
import com.wre.game.api.data.entity.UserActionAd;
import com.wre.game.api.data.entity.UserActionGame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserActionAdDaoTest {

    @Autowired
    private UserActionAdDao userActionAdDao;

    /**
     * test insert
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        UserActionAd user = new UserActionAd();
        user.setAdType("video");
        user.setAdPosition("left top");
        user.setAppId("to id");
        user.setAppName("to name");
        user.setChannel("channel");
        user.setUserId(20l);
        userActionAdDao.insertUseActionAd(user);
    }


}