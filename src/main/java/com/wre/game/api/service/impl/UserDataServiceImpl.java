package com.wre.game.api.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.constants.ApiResponseMessageBuilder;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.dao.UserDao;
import com.wre.game.api.dao.UserGameDataDao;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.service.UserDataService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDataServiceImpl implements UserDataService {
    private static final Logger logger = LoggerFactory.getLogger(UserDataServiceImpl.class);
    private static final int RANK_CACHE_EXPIRE_SECONDS = 60 * 60;
    @Resource
    DataComponent dataComponent;
    @Resource
    private UserDao userDao;
    @Resource
    private UserGameDataDao userGameDataDao;
    @Resource
    private SessionComponent sessionComponent;
    @Value("${backend.api.token.secret}")
    private String tokenSecret;

    @Override
    public RtCode saveUserGameData(UserGameData userGameData) {
        UserGameData exist = userGameDataDao.selectUserGameData(userGameData.getUuid(), userGameData.getAppId());

        int i = 0;
        if (exist != null) {
            i = userGameDataDao.updateUserGameData(userGameData);
        } else {
            i = userGameDataDao.insertUserGameData(userGameData);
        }

        if (i > 0) {
            return RtCode.RT_SUCCESS;
        } else {
            throw new ApiException(RtCode.RT_INTERNAL_ERROR);
        }
    }

    @Override
    public List<UserRankDTO> selectGameDataRank(String appId, Integer limit) {

        String rankJson = dataComponent.getGameRankData(appId);

        List<UserRankDTO> list = new ArrayList<>();

        Gson gson = new Gson();
        if (StringUtils.isBlank(rankJson)) {
//            System.out.println("load from db");
            list = userGameDataDao.selectGameDataRank(appId, limit);

            //set rank int data
            if (list != null && list.size() > 0) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    UserRankDTO userRankDTO = list.get(i);
                    userRankDTO.setRank(i + 1);
                }
            }

            String data = gson.toJson(list);
            dataComponent.setGameRankData(appId, data, RANK_CACHE_EXPIRE_SECONDS);
        } else {
//            System.out.println("load from cache");
            list = gson.fromJson(rankJson, new TypeToken<List<UserRankDTO>>() {
            }.getType());
        }
        return list;
    }

    @Override
    public ResponseEntity<ApiResponseMessage> clearGameData(String uuid) {
        try{
            int result=userGameDataDao.clearGameData(uuid);
            return new ResponseEntity<>( ApiResponseMessageBuilder.build(RtCode.RT_SUCCESS), RtCode.RT_SUCCESS.getHttpStatus());

        }catch (Exception e){
            return new ResponseEntity<>( ApiResponseMessageBuilder.build(RtCode.RT_INTERNAL_ERROR), RtCode.RT_INTERNAL_ERROR.getHttpStatus());
        }

    }
}
