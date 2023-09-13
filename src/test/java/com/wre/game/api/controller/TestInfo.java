package com.wre.game.api.controller;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("bd")
public class TestInfo {
    private static final Logger logger = LoggerFactory.getLogger(TestInfo.class);
    @Before
    public void init() {
        logger.info("开始测试-----------------");
    }

    @After
    public void after() {
        logger.info("测试结束-----------------");
    }

    public static Logger getLogger() {
        return logger;
    }

    @Test
    public void test(){
        getLogger().info("被迫测试");
    }
}