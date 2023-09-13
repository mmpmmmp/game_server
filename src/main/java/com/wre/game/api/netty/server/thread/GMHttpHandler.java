package com.wre.game.api.netty.server.thread;

import java.util.HashMap;

import com.wre.game.api.entity.RechargeUtil;
import com.wre.game.api.message.MessageResp;
import com.wre.game.api.message.NullObject;
import com.wre.game.api.message.ResultCode;
import com.wre.game.api.netty.entity.RechargeResult;
import com.wre.game.api.netty.message.JsonUtil;
import com.wre.game.api.netty.server.http.AbsHttpServerHandler;
import com.wre.game.api.netty.server.http.RechargeHttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GMHttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GMHttpHandler.class);

	public static Object action(HashMap<String, String> hashMap) {
		try {
			return RechargeHttpHandler.actionString(hashMap);
		} catch (Exception e) {
			String str = String.format("Server %d execute Recharge error:", RechargeUtil.getPort()) + e.getMessage();
			LOGGER.error(str,e);
			return  new RechargeResult(-100, "其它错误");
		}
	}

}
