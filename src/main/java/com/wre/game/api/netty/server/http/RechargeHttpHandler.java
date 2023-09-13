package com.wre.game.api.netty.server.http;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.LogManager;

import com.alibaba.fastjson.JSON;
import com.wre.game.api.netty.entity.RechargeInfo;
import com.wre.game.api.netty.entity.RechargeResult;
import com.wre.game.api.netty.server.manager.RechargeManager;
import com.wre.game.api.netty.server.pool.ManagePool;
import com.wre.game.api.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RechargeHttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(RechargeHttpHandler.class);


	public static RechargeResult actionString(HashMap<String, String> hashMap) {
		RechargeInfo rechargeInfo = JSON.parseObject(JSON.toJSONString(hashMap), RechargeInfo.class);
		return SpringContextHolder.getBean(RechargeManager.class).reqRechargeCallback(rechargeInfo);
	}

	public static void main(String[] args) {
		String string = "{\"uugameId\":101,\"amount\":1.00,\"gameCustomInfo\":\"201807061818\",\n" +
				"\"productId\":\"10100001\",\"orderId\":\"131018070664800724511\",\"sign\":\"ZBhj8I\n" +
				"IRynVYSLSs+f9J4aWzmom0hT2QPZmCwNtcpZiGxV6jp2XZx0XIaUNT7nA4fIS/zStg/3NLh\n" +
				"0B58/Tdq0GE5WVCHI5KUa5zAugBdGxuAd3QVB1W/SZqT5MK1mrqPvvuedvsr8ohUA1EefjF\n" +
				"Sm9kBm6lLu7HKW6BqIRiL08=\",\"signtype\":\"RSA\",\"state\":1,\"userId\":\"9416239\n" +
				"8\",\"serverId\":\"1\",\"channelId\":\"1000100013\"}";
		RechargeInfo rechargeInfo = JSON.parseObject(string, RechargeInfo.class);
		SpringContextHolder.getBean(RechargeManager.class).reqRechargeCallback(rechargeInfo);
	}
}
