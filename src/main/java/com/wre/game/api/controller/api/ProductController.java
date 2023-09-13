package com.wre.game.api.controller.api;

import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.ApiResource;
import com.wre.game.api.data.JunhaiResponse;
import com.wre.game.api.service.UserDataService;
import com.wre.game.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_PRODUCT)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * Save user's game data with text
     *
     * @return
     */
    @RequestMapping(value = "/items/deliver", method = {RequestMethod.POST})
    @ResponseBody
    public String deliverProductItems() {
        return "SUCCESS";
    }


    /**
     * Save user's game data with text
     *
     * @return
     */
    @RequestMapping(value = "/items/deliver/junhai", method = {RequestMethod.GET})
    @ResponseBody
    public JunhaiResponse deliverProductItemsJunhai() {
        JunhaiResponse junhaiResponse = new JunhaiResponse();
        junhaiResponse.setRet(1);// 0: 失败；1：成功
        return junhaiResponse;
    }
}
