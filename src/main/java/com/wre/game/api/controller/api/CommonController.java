package com.wre.game.api.controller.api;

import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.ApiResource;
import com.wre.game.api.constants.ApiResponseDataMessage;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.data.GameInfo;
import com.wre.game.api.entity.RechargeAndroidRes;
import com.wre.game.api.message.RechargeMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @menu CommonController
 */
@RestController
@RequestMapping(value = ApiResource.API_HOME)
public class CommonController {

    /**
     * 首页
     */
    @RequestMapping("/")
    public String home() {
        return "hello";
    }


    /**
     * 测试游戏是否存在
     * @param appId
     * @return
     */
    @GetMapping("/gameapi/v1/getTimeStamp")
    public ResponseEntity<ApiResponseMessage> getTimeStamp(@Param("appId") String appId){
        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);
        if (gameInfo == null) {
          return   new ResponseEntity<ApiResponseMessage>(new ApiResponseDataMessage(new ApiResponseMessage("500",
                    "appId no config", "501", "appId no config"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF)), HttpStatus.OK);
        }
        return new ResponseEntity<ApiResponseMessage>(new ApiResponseDataMessage(
                new ApiResponseMessage("200", "success", "200", "success"),
                System.currentTimeMillis()), HttpStatus.OK);
    }
}
