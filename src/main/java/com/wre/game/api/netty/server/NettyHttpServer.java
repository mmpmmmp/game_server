package com.wre.game.api.netty.server;

import java.util.Locale;

import com.wre.game.api.netty.server.http.AbsHttpServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.SystemPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * httpserver
 * 
 * @author zs
 * @date 2020-03-27 16:09:25
 */
public class NettyHttpServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpServer.class);

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void start(int port, AbsHttpServerHandler handler) throws Exception {
		ServerBootstrap bootstrap = new ServerBootstrap();

		Class<? extends ServerChannel> serverChannel = null;

		int defaultBossThread = Runtime.getRuntime().availableProcessors() <= 4 ? 1 : 2;
		DefaultThreadFactory defaultBossThreadFactory = new DefaultThreadFactory("httpBoss");
		int defaultWorkThread = Runtime.getRuntime().availableProcessors() <= 10 ? 2 : Runtime.getRuntime().availableProcessors() / 5;
		DefaultThreadFactory defaultWorkThreadFactory = new DefaultThreadFactory("httpWork");
		// 在linux上使用Epoll模型
		String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
//		if (name.startsWith("linux")) {
//			bossGroup = new EpollEventLoopGroup(defaultBossThread, defaultBossThreadFactory);
//			workerGroup = new EpollEventLoopGroup(defaultWorkThread, defaultWorkThreadFactory);
//			serverChannel = EpollServerSocketChannel.class;
//		} else {
			bossGroup = new NioEventLoopGroup(defaultBossThread, defaultBossThreadFactory);
			workerGroup = new NioEventLoopGroup(defaultWorkThread, defaultWorkThreadFactory);
			serverChannel = NioServerSocketChannel.class;
//		}

		bootstrap.group(bossGroup, workerGroup).channel(serverChannel)
				// socket
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG)).addLast(new HttpResponseEncoder()).addLast(new HttpRequestDecoder())
								// 注意,对分块消息的处理机制 大小测试,例如刷新配置表大数据量等操作.
								// 特殊需求可以通过4.1 重写handleOversizedMessage方法来解决
								.addLast(new HttpObjectAggregator(1024 * 1024 * 64)).addLast(handler);
					}
				});
		setOption(bootstrap);
		// 同步等待绑定成功...
		ChannelFuture future = bootstrap.bind(port).sync();

		// 监听关闭事件
		future.channel().closeFuture().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				// 抛出监听停止事件，注意该事件的执行线程
				LOGGER.info("线程" + Thread.currentThread().getName() + " 执行HTTP监听关闭事件: " + port);
			}
		});

		LOGGER.info("start GM Http server listen on : " + port);
	}

	/** 设置参数 */
	@SuppressWarnings("deprecation")
	public static void setOption(ServerBootstrap bootstrap) {
		// 注意:此处参数HTTP与TCP一致,如需单独修改,另抽象方法进行调用

		// option() 方法用于设置监听套接字。childOption()则用于设置连接到服务器的客户端套接字。
		// option存在于AbstractBootstrap，提供给NioServerSocketChannel用来接收进入的连接。 boss线程
		// 作用于ServerChannel
		// childOption存在于ServerBootstrap ，提供给ServerChannel接收已经建立的连接。worker线程 作用于Channel

		// netty参数
		// 压测时观察netty的包外内存大小
		// -XX:MaxDirectMemorySize 未设置
		// 则使用了系统默认值:新生代的最大值-一个survivor的大小+老生代的最大值，也就是我们设置的-Xmx的值里除去一个survivor的大小
		// 并且必须大于96M
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		// 此设置为默认设置
		bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
		bootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);

		// 系统参数
		// 根据实际情况调整该大小
		bootstrap.option(ChannelOption.SO_BACKLOG, 65535);
		// 端口复用
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);

		bootstrap.childOption(ChannelOption.SO_SNDBUF, 10240);
		bootstrap.childOption(ChannelOption.SO_RCVBUF, 2560);
		// 关闭Socket的延迟时间，默认值为-1.尽量保证当前缓冲区的数据发送完成
		bootstrap.childOption(ChannelOption.SO_LINGER, -1);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

		// if (isLinux) {
		// // 隶属于同一个用户（防止端口劫持）的多个进程/线程共享一个端口，同时在内核层面替上层应用做数据包进程/线程的处理均衡
		// bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
		// }
	}

	public void stop() {
		LOGGER.info("http server close begin...");
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		LOGGER.info("http server stop end.");
	}
}
