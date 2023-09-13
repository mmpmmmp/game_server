package com.wre.game.api.netty.server.pool;


import com.wre.game.api.netty.server.manager.RechargeManager;

/**
 * 单例管理器
 * @author {author}
 * @date {date}
 * @version Commuication Auto Maker
 * 
 */
public class ManagePool {
	private static final ManagePool MANAGE_POOL = new ManagePool();

	public static ManagePool getInstance() {
		return MANAGE_POOL;
	}

}
