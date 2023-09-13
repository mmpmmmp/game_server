package com.wre.game.api;

import com.wre.game.api.dao.UserLoginHistoryDao;
import com.wre.game.api.data.entity.UserLoginHistory;
import com.wre.game.api.data.entity.UserRetention;
import com.wre.game.api.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WechatGameApiApplication.class)
public class UserLoginHistoryDaoTest {
    @Autowired
    private UserLoginHistoryDao userLoginHistoryDao;

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
        UserLoginHistory ulh = new UserLoginHistory();
        ulh.setAppId("app_id");
        ulh.setAppName("app_name");
        ulh.setAppVersion("app_version");
        ulh.setChannel("channel");
        ulh.setLoginTime(new Date());
        ulh.setUserId(1l);
        ulh.setNewUserYn("y");
        ulh.setUserCreatedAt(new Date(118,8,1,0,0,0));
        ulh.setUuid("uuid");
        ulh.setLoginTime(new Date(118,8,1,0,0,0));
        userLoginHistoryDao.insertUserLoginHistory(ulh);


    }

    @Test
    public void testBackup() throws Exception {

        Date startTime = DateUtil.getBeginDayOfMonth();

        Date endTime = DateUtil.getEndDayOfMonth();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String month = sdf.format(startTime);

        userLoginHistoryDao.backupUserLoginHistory("user_login_history_"+month, startTime, endTime);
    }

    @Test
    public void testDelete() throws Exception{
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -60);
        Date date = calendar.getTime();
        System.out.println(date);
        userLoginHistoryDao.deleteUserLoginHistoryBefore(null);
    }

    @Test
    public void testNewUserRentention() throws Exception{
        String dateStr = "2018-11-18";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);
        Date startTime = DateUtil.getDayBegin(date);
        Date endTime = DateUtil.getDayEnd(date);

        List<UserRetention> list =userLoginHistoryDao.selectNewUserRentention(startTime,endTime, null, "1", "all");
        list.stream().forEach(x -> System.out.println(x));

        List<UserRetention> list2 =userLoginHistoryDao.selectActiveUserRentention(startTime,endTime, null, "1", "all");
        list2.stream().forEach(x -> System.out.println(x));
    }

}