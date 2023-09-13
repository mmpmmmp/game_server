package com.wre.game.api.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.constants.ApiResponseDataMessage;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.constants.ApiResponseMessageBuilder;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.dao.GameCodeMapper;
import com.wre.game.api.dao.GameCodeUserMapper;
import com.wre.game.api.data.entity.GameCodeUser;
import com.wre.game.api.data.entity.GameCodeWithBLOBs;
import com.wre.game.api.data.entity.RechargeDataInfo;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.entity.IosRechargeInApp;
import com.wre.game.api.entity.IosRechargeParam;
import com.wre.game.api.entity.RechargeAndroidRes;
import com.wre.game.api.message.RechargeMessage;
import com.wre.game.api.service.GameCodeService;
import com.wre.game.api.util.Fn;
import com.wre.game.api.util.IosVerifyUtil;
import com.wre.game.api.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class GameCodeServiceImpl implements GameCodeService {
    private static final Logger logger = LoggerFactory.getLogger(GameCodeServiceImpl.class);
    @Resource
    GameCodeMapper gameCodeService;
    @Resource
    GameCodeUserMapper gameCodeUserMapper;

    @Override
    public ApiResponseDataMessage getAward(String userId,String code,String appId) {
        try {
            GameCodeWithBLOBs gameCode=gameCodeService.selectByCode(code,appId);
            /**判断是否存在兑换码*/
            if(gameCode==null){
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no code" , "301", "no code"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            /**判断是否开启*/
            if (!isStartTimeCode(gameCode)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no activation" , "302", "no activation"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            /**判断是否过期*/
            if (!isEndTimeCode(gameCode)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no activation" , "303", "no no activation"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            /**判断是否使用*/
            if (!isRole(userId, gameCode)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no use" , "305", "no use"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            /**判断领取次数是否达到上限*/
            if (!isCodeCountMax(gameCode)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "count Max" , "304", "count Max"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            int count=gameCodeUserMapper.selectByUser(gameCode.getId(),userId);
            if(count>0){
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "used" , "306", "used"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            return new ApiResponseDataMessage(new ApiResponseMessage("200", "success" , "200", "success"),gameCode.getAwards());

        } catch (Exception e) {
            logger.error("兑换码使用错误：" + code+e.getMessage(),e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail" , "fail", "204"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    public ApiResponseDataMessage useCode(String userId, String code,String appId) {
        try {
            GameCodeWithBLOBs gameCode=gameCodeService.selectByCode(code,appId);
            /**判断是否存在兑换码*/
            if(gameCode==null){
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no code" , "301", "no code"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            int count=gameCodeUserMapper.selectByUser(gameCode.getId(),userId);
            if(count>0){
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "used" , "306", "used"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));

            }
            GameCodeUser codeUser=new GameCodeUser();
            codeUser.setCodeId(gameCode.getId());
            codeUser.setUserId(userId);
            codeUser.setUseTime(new Date());
            addCount(gameCode);
            int i=gameCodeUserMapper.insertSelective(codeUser);
            if(i<1){
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail" , "fail", "204"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            return new ApiResponseDataMessage(new ApiResponseMessage("200", "success" , "200", "success"),new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
        } catch (Exception e) {
            logger.error("兑换码使用错误：" + code+"_"+userId+"_"+e.getMessage(),e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail" , "fail", "204"),new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));

    }


    /**
     * 添加兑换码使用次数
     *
     */
    public  void addCount(GameCodeWithBLOBs gameCode) {
        gameCode.setUseCount(gameCode.getUseCount() + 1);
        gameCodeService.updateByPrimaryKeySelective(gameCode);
    }

    /**
     * 判断兑换码是否激活
     * true 激活
     */
    public boolean isStartTimeCode(GameCodeWithBLOBs code) {
        if (StringUtils.isBlank(code.getStartTime())) {
            return true;
        }
        int now = TimeUtil.getNowTime();
        int codeStartTime = Fn.toInt(code.getStartTime());
        return now >= codeStartTime;
    }

    /**
     * 判断兑换码是否过期
     */
    public boolean isEndTimeCode(GameCodeWithBLOBs code) {
        if (StringUtils.isBlank(code.getEndTime())) {
            return true;
        }
        int now = TimeUtil.getNowTime();
        int codeEndTime = Fn.toInt(code.getEndTime());
        return now <= codeEndTime;
    }

    /**
     * 判断兑换码是否是专属领取 (次数)
     */
    public boolean isRole(String userId, GameCodeWithBLOBs code) {
        if (StringUtils.isBlank(code.getRoleids())||"[]".equals(code.getRoleids())) {
            return true;
        }
        cn.hutool.json.JSONArray roleIds = JSONUtil.parseArray(code.getRoleids());
        Integer user=Fn.toInt(userId);
        return roleIds.contains(user);
    }

    /**
     * 判断兑换码是否能使用 (次数)
     * true 能够使用
     */
    public boolean isCodeCountMax(GameCodeWithBLOBs code) {
        if (code.getMaxCount() == 0) {
            return true;
        }

        return code.getUseCount() < code.getMaxCount();
    }
}
