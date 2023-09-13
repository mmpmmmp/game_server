package com.wre.game.api.netty.start;

import com.wre.game.api.entity.RechargeUtil;
import com.wre.game.api.netty.server.NettyHttpServer;
import com.wre.game.api.netty.server.http.AbsHttpServerHandler;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Scanner;

public class GameServer {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GameServer.class);
	private static final GameServer GAME_SERVER = new GameServer();


	/** 服务器状态1白名单2运行3停服中 */
	public static int runType = 0;
	/** 是否linux true是false不是 */
	public static boolean isLinux = false;

	public static String defaultSystem="linux";

	public static String SystemName=SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();



	public static GameServer getInstance() {
		return GAME_SERVER;
	}

	public void start() throws Exception {
		if (SystemName.startsWith(defaultSystem)) {
			isLinux = true;
		}
		/**开始时间设定*/
		long start = System.currentTimeMillis();
		/**启动充值线程*/
		logger.info("启动充值-GM Http");
		new NettyHttpServer().start(RechargeUtil.getPort(), new AbsHttpServerHandler());
		// 钩子
		jvmHookStart();
		// 获取JVM名字
		logger.info("JVM名字:  {}", System.getProperty("java.vm.name"));
		shenmi();
		// 控制台,jar包运行会报错
		if (!isLinux) {
			new Thread(new ConsleRunnable()).start();
		}

		long end = System.currentTimeMillis();
		logger.info("========服务器启动成功，serverId:{},耗时 {}s,服务器状态:{}========", RechargeUtil.getPort(), (end - start) / 1000, runType == 1 ? "白名单" : "正常启动");
	}

	//版本记录
	//20200424 之前已经提交了很多的版本了，没有记录，从现在开始
	public static void shenmi() {
		logger.info("");
		logger.info("    (o)--(o)");
		logger.info("   /.______.\\");
		logger.info("   \\________/");
		logger.info("  ./        \\.");
		logger.info(" (   No Bug ! )");
		logger.info("  \\ \\_\\\\//_/ /");
		logger.info("   ~~  ~~  ~~  ");
	}

	public void jvmHookStart() {
		logger.info("The JVM Hook is started");
		Thread hook = new Thread() {
			public void run() {
				try {
					logger.info("=====停服钩子开始停服=========");
					GameServer.runType = 3;
					logger.info("The JVM Hook is stoped");
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		};
		hook.setName("JVM-Hook");
		Runtime.getRuntime().addShutdownHook(hook);
	}

}

@SuppressWarnings("resource")
class ConsleRunnable implements Runnable {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConsleRunnable.class);

	@Override
	public void run() {
		try {
		logger.info("IDE : Console Opened");
		Scanner scanner = new Scanner(System.in);
			while (true) {
				String cmd = scanner.next();
				runCommand(cmd);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void runCommand(String input) {
		if (StringUtils.isEmpty(input)) {
			return;
		}
	}
}