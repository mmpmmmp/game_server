package com.wre.game.api.service.impl;

import com.wre.game.api.context.AbTestInfoStarted;
import com.wre.game.api.dao.AbTestInfoMapper;
import com.wre.game.api.data.entity.AbTestInfo;
import com.wre.game.api.message.MessageResp;
import com.wre.game.api.message.NullObject;
import com.wre.game.api.message.ResultCode;
import com.wre.game.api.netty.message.JsonUtil;
import com.wre.game.api.service.AbTestInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class AbTestInfoServiceImpl implements AbTestInfoService {
    private static final Logger logger = LoggerFactory.getLogger(AbTestInfoServiceImpl.class);
    @Resource
    private AbTestInfoMapper abTestInfoMapper;

    @Resource
    AbTestInfoStarted abTestInfoStarted;

    @Override
    public MessageResp getABList() {
        try {
            List<AbTestInfo> result=abTestInfoMapper.selectByAll();
            return JsonUtil.getResponse(ResultCode.SUCCESS_CODE,"",result);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonUtil.getResponse(ResultCode.ERROR_CODE,"删除ab有问题", NullObject.getInstance());
        }
    }

    @Override
    public MessageResp delAB(List<Integer> ids) {
        try {
            if(ids!=null&&!ids.isEmpty()){
                for(Integer id:ids){
                    abTestInfoMapper.deleteByPrimaryKey(id);
                }
            }
            abTestInfoStarted.reload();
            return JsonUtil.getResponse(ResultCode.SUCCESS_CODE,"", "");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonUtil.getResponse(ResultCode.ERROR_CODE,"删除ab有问题", NullObject.getInstance());
        }

    }

    @Override
    public MessageResp addAB(AbTestInfo abTestInfo) {
        try {
            abTestInfo.setCreateTime(new Date());
            abTestInfo.setUpdateTime(new Date());
            abTestInfoMapper.insertSelective(abTestInfo);
            abTestInfoStarted.add(abTestInfo);
            return JsonUtil.getResponse(ResultCode.SUCCESS_CODE,"","");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return JsonUtil.getResponse(ResultCode.ERROR_CODE,"添加ab有问题", NullObject.getInstance());
        }

    }
}
