package com.wre.game.api.netty;

import com.wre.game.api.WechatGameApiApplication;
import com.wre.game.api.netty.start.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;

@Configuration
public class Start implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WechatGameApiApplication.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            logger.info("Recharge Server Start Success");
            // 启动充值服务器服务器
            GameServer.getInstance().start();
//            logger.info("充值服务器关闭");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }

    }
}
