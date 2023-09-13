package com.wre.game.api.controller;


import com.wre.game.api.entity.RechargeInfo;
import org.junit.Test;

import javax.annotation.Resource;

public class SeedMail extends TestInfo {

    @Resource
    private RechargeInfo serverInfo;

    @Test
    public void yml(){
        System.out.println(serverInfo.getPort());
    }

}
