package com.wre.game.api.entity;

import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
public class RechargeProxy {

    public RechargeProxy(RechargeInfo rechargeInfo) {
        RechargeUtil.initStaticProperties(rechargeInfo);
    }
}
