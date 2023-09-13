package com.wre.game.api;

import com.wre.game.api.dao.UserActionShareDao;
import com.wre.game.api.data.entity.UserActionShare;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserActionShareDaoTest {

    @Autowired
    private UserActionShareDao userActionShareDao;

    /**
     * test insert
     *
     * @throws Exception
     */
    @Test
    public void testInsert() throws Exception {
        UserActionShare user = new UserActionShare();
        user.setAppId("appid");
        user.setAppName("appname");
        user.setChannel("wechat");
        user.setShareType("1");
        user.setUserId(20l);
        userActionShareDao.insertUseActionShare(user);
    }


}