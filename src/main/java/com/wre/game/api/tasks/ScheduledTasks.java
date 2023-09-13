package com.wre.game.api.tasks;

import com.wre.game.api.dao.UserLoginHistoryDao;
import com.wre.game.api.dao.UserMailInfoDao;
import com.wre.game.api.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    private UserLoginHistoryDao userLoginHistoryDao;
//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {
//        System.out.println("现在时间：" + dateFormat.format(new Date()));
//    }

    @Resource
    UserMailInfoDao userMailInfoDao;

    /**
     * 每月1号2:00备份上个月的登录历史记录
     */
//    @Scheduled(cron = "0 0 2 1 * ? ")
//    public void backupUserLoginHistory() {
//        Date startTime = DateUtil.getBeginDayOfLastMonth();
//        Date endTime = DateUtil.getBeginDayOfLastMonth();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//        String monthStr = sdf.format(startTime);
//        String tableName = "user_login_history_" + monthStr;
//
//        logger.info("backupUserLoginHistory, tableName:{}, startTime:{}, endTime:{}", tableName, startTime, endTime);
//        int i = userLoginHistoryDao.backupUserLoginHistory(tableName, startTime, endTime);
//        logger.info("backupUserLoginHistory, back up history records number:{}", i);
//    }

    /**
     * 每天2:00, 删除90天以前的登录历史记录，注意备份任务@backupUserLoginHistory的时间配置
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteOldUserLoginHistory() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -90);
        Date date = calendar.getTime();

        logger.info("deleteOldUserLoginHistory before :{}, startTime:{}, endTime:{}", date);
        userLoginHistoryDao.deleteUserLoginHistoryBefore(date);
    }

    /**
     * 每隔半小时清理超过72小时已经领取奖励了的邮件记录
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void deleteAwardedUserMailInfo() {
        logger.warn("start clean Awarded User Mail Info......");
        int result=userMailInfoDao.deleteAwardedUserMailInfo();
        logger.warn("clean mail result:" +result);
    }

}