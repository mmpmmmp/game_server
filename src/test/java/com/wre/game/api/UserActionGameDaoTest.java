package com.wre.game.api;

import com.wre.game.api.dao.UserActionGameDao;
import com.wre.game.api.dao.UserActionShareDao;
import com.wre.game.api.data.entity.UserActionGame;
import com.wre.game.api.data.entity.UserActionShare;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserActionGameDaoTest {

    @Autowired
    private UserActionGameDao userActionGameDao;

    /**
     * test insert
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        UserActionGame user = new UserActionGame();
        user.setFromAppId("from");
        user.setFromAppName("from appname");
        user.setToAppId("to id");
        user.setToAppName("to name");
        user.setUserId(20l);
        userActionGameDao.insertUseActionGame(user);
    }


}