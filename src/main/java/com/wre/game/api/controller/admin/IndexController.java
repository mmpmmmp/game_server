package com.wre.game.api.controller.admin;

import com.wre.game.api.message.MessageResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test
 */
@RestController
@RequestMapping("/index")
public class IndexController {


    /**
     * 首页
     */
    @GetMapping()
    public MessageResp index() {
        return new MessageResp();
    }

}
