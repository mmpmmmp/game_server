package com.wre.game.api.controller.api;

import com.wre.game.api.constants.ApiResource;
import com.wre.game.api.data.entity.AbTestInfo;
import com.wre.game.api.message.MessageResp;
import com.wre.game.api.service.AbTestInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ABTestInfoController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_LOGIN_EVENT)
public class ABTestInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ABTestInfoController.class);

    @Resource
    AbTestInfoService abTestInfoService;

    /**
     * 获取AbTests
     */
    @RequestMapping(value = "/getAbTests", method = {RequestMethod.GET})
    public MessageResp getAbTests() {
        return abTestInfoService.getABList();
    }




    /**
     * 新增AbTests
     */
    @RequestMapping(value = "/addAbTest", method = {RequestMethod.POST})
    public MessageResp addAbTest( @RequestBody AbTestInfo body) {
        return abTestInfoService.addAB(body);
    }




    /**
     * 删除AbTests
     */
    @RequestMapping(value = "/delAbTests", method = {RequestMethod.POST})
    public MessageResp delAbTests( @RequestBody List<Integer> body) {
        return abTestInfoService.delAB(body);
    }

}
