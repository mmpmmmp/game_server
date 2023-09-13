package com.wre.game.api;

import com.wre.game.api.dao.UserDao;
import com.wre.game.api.data.dto.UserShareFromDTO;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.data.entity.UserShareFrom;
import com.wre.game.api.util.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserDaoTest {
    {
        System.setProperty("spring.profiles.active","local");
    }
    @Autowired
    private UserDao userDao;

    /**
     * 新增用户
     *
     * @throws Exception
     */
    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        user.setOpenId(UUID.randomUUID().toString());
        user.setNickName("zhangsan");
        user.setShareFromImage("image001.png");
        user.setSceneId("scene_001");
        userDao.insertUser(user);
        System.out.println(user);
    }

    /**
     * 删除用户
     *
     * @throws Exception
     */
    @Test
    public void testDelUser() throws Exception {
        userDao.deleteUserById(1l);
    }

    /**
     * 修改用户信息
     *
     * @throws Exception
     */
    @Test
    public void testUpdUser() throws Exception {
        User user = new User();
        user.setOpenId("zhangsan999");
        user.setNickName("n");
        user.setUserId(10l);
        userDao.updateUserById(user);
    }

    /**
     * 查询用户
     *
     * @throws Exception
     */
    @Test
    public void testQueryUser() throws Exception {
        User user = userDao.selectUserById(3L);
        System.out.println(user);
    }

    /**
     * 查询所有用户
     *
     * @throws Exception
     */
    @Test
    public void testQueryUserList() throws Exception {
        List<User> list = userDao.selectUserList();
        for (User user : list) {
            System.out.println(user.getNickName());
        }
    }

    /**
     * 查询朋友
     *
     * @throws Exception
     */
    @Test
    public void testQueryShareStatistics() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        String uuidFrom = TokenUtil.generateUuid();
        String uuidTo = TokenUtil.generateUuid();
        String appId = "appId";
        String appName = "appName";
        String shareType = "1";

        userDao.insertUserShareFrom(uuidFrom, uuidTo, shareType, appId, appName);

        Integer i = userDao.selectExistShareFrom(uuidFrom, uuidTo, shareType, appId);

        System.out.println("i=" + i);

        List<UserShareFromDTO> list = userDao.selectShareStatistics("10227adc769a444690d46ce47fac8501", shareType, appId, startTime, endTime);
        System.out.println(list);
        for (UserShareFromDTO user : list) {
            System.out.println(user);
        }
    }

    /**
     * 查询朋友
     *
     * @throws Exception
     */
    @Test
    public void testQueryShareStatisticsFunctional() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();

        String uuidFrom = TokenUtil.generateUuid();
        String uuidTo = TokenUtil.generateUuid();
        String appId = "appId";
        String appName = "appName";
        String shareType = "1";

        userDao.insertUserShareFromFunctional(uuidFrom, uuidTo, shareType, appId, appName);

        Integer i = userDao.selectExistShareFromFunctional(uuidFrom, uuidTo, shareType, appId);
        UserShareFrom usf = userDao.selectShareFromFunctional(uuidFrom, uuidTo, shareType, appId);

        System.out.println("i=" + i);
        System.out.println(usf);

        List<UserShareFromDTO> list = userDao.selectShareStatisticsFunctional(uuidFrom, shareType, appId, startTime, endTime);
        System.out.println(list);
        for (UserShareFromDTO user : list) {
            System.out.println(user);
        }
    }

}