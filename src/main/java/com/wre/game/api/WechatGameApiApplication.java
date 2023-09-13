package com.wre.game.api;

import com.wre.game.api.entity.RechargeInfo;
import com.wre.game.api.entity.RechargeUtil;
import com.wre.game.api.netty.start.GameServer;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

@SpringBootApplication
@MapperScan("com.wre.game.api.dao")
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties({RechargeInfo.class})
public class WechatGameApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(WechatGameApiApplication.class);
	public static void main(String[] args) throws Exception {
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		try (FileWriter writer = new FileWriter("server.pid")) {
			writer.write(pid);
			writer.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}

		SpringApplication.run(WechatGameApiApplication.class, args);
		logger.info("Server Start Success");
	}
}
