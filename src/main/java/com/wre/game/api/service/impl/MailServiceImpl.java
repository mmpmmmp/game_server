package com.wre.game.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.constants.ApiResponseDataMessage;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.controller.api.AdminController;
import com.wre.game.api.dao.MailConfigDao;
import com.wre.game.api.dao.UserMailInfoDao;
import com.wre.game.api.data.dto.UserMailDTO;
import com.wre.game.api.data.entity.UserMailInfo;
import com.wre.game.api.data.param.MailChangeParam;
import com.wre.game.api.data.param.MailContentParam;
import com.wre.game.api.entity.RechargeAndroidRes;
import com.wre.game.api.message.RechargeMessage;
import com.wre.game.api.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MailServiceImpl implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    @Resource
    UserMailInfoDao userMailInfoDao;
    @Resource
    MailConfigDao mailConfigDao;

    @Override
    public ResponseEntity<ApiResponseMessage> getUserMail(String userId, String appId,String countryCode) {
        //获取用户对应的邮件
        //有傻子非跟我扯不同时刻获取的时间戳是一样的，直接就默认服务器时间戳
        List<UserMailDTO> userMailDTOList = mailConfigDao.getUserMail(userId, appId, System.currentTimeMillis()/1000);
        for (UserMailDTO userMailDTO : userMailDTOList) {
            //邮件内容多语言处理
            List<MailContentParam> mailContentParams = JSONObject.parseArray(userMailDTO.getContent(), MailContentParam.class);
            Map<String, List<MailContentParam>> collect = mailContentParams.stream().collect(Collectors.groupingBy(item -> item.getCountry()));
            List<MailContentParam> mailContentParams1 = collect.get(countryCode);
            if (mailContentParams1!=null&&mailContentParams1.size()>0){
                userMailDTO.setContent(mailContentParams1.get(0).getContent());
                userMailDTO.setTitle(mailContentParams1.get(0).getTitle());
            }
            //默认取US
            else {
                mailContentParams1=collect.get("US");
                userMailDTO.setContent(mailContentParams1.get(0).getContent());
                userMailDTO.setTitle(mailContentParams1.get(0).getTitle());
            }
            //没有读过的插入一条读取记录
            if (userMailDTO.getHasInsert() != 1) {
                UserMailInfo userMailInfo = new UserMailInfo();
                userMailInfo.setCreateTime(new Date());
                userMailInfo.setUpdateTime(new Date());
                userMailInfo.setUserId(Long.valueOf(userId));
                userMailInfo.setState(0);
                userMailInfo.setMailId(userMailDTO.getMailId());
                userMailInfo.setAppId(appId);
                userMailInfo.setDelFlag(0);
                userMailInfoDao.insert(userMailInfo);
            }
        }
        return new ResponseEntity<ApiResponseMessage>(new ApiResponseDataMessage(
                new ApiResponseMessage("200", "success", "200", "success"),
                userMailDTOList), HttpStatus.OK);
    }

    @Override
    public ApiResponseDataMessage changeMailState(MailChangeParam mailChangeParam) {
        UserMailInfo userMailInfo = userMailInfoDao.selectIfExit(mailChangeParam.getUserId(), mailChangeParam.getMailId());
        //没有对应的用户邮件信息
        if (userMailInfo == null) {
            return new ApiResponseDataMessage(new ApiResponseMessage("200", "no user mail info", "401", "no user mail info"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
        }
        //禁止回退
        if (userMailInfo.getState() > mailChangeParam.getState()) {
            return new ApiResponseDataMessage(new ApiResponseMessage("200", "state can not go back", "402", "state can not go back"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
        }
        int result = userMailInfoDao.changeState(mailChangeParam);
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "success", "200", "success"), new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
    }
}
