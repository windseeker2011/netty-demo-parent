package com.windseeker2011.netty.chapter2;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.common.collect.ImmutableList;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

public class NettyService {

	private static final int port = 8888;

	private static final int readerIdleTimeSeconds = 0;

	private static final int writerIdleTimeSeconds = 0;

	private static final int allIdleTimeSeconds = 0;

	/**
	 * 检查Netty是否启动
	 */
	private static volatile boolean isRunning = false;

	private NioEventLoopGroup parentGroup;

	private NioEventLoopGroup childGroup;

	private NioEventLoopGroup handlerGroup;

	private Channel channel;

	private MyServerHandler serverHandler;

	private AtomicInteger index = new AtomicInteger(0);

	public void run() {
		/*
		 * 检查Netty服务是否启动
		 */
		if (!isRunning) {
			synchronized (NettyService.class) {
				if (!isRunning) {// 双重判断，提升性能
					try {
						this.parentGroup = new NioEventLoopGroup();
						this.childGroup = new NioEventLoopGroup();
						this.handlerGroup = new NioEventLoopGroup();
						// MyServerHandler
						this.serverHandler = new MyServerHandler();
						// ServerBootstrap
						ServerBootstrap serverBootstrap = new ServerBootstrap();
						serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
								// 初始化执行
								.handler(new ChannelInitializer<ServerChannel>() {
									@Override
									protected void initChannel(ServerChannel ch) throws Exception {
										System.out.println("Netty服务 初始化配置项");
									}
								})
								// 客户端连接后执行
								.childHandler(new ChannelInitializer<SocketChannel>() {
									@Override
									protected void initChannel(SocketChannel ch) throws Exception {
										ch.pipeline()//
												.addLast(new MyMessageEncoder())//
												.addLast(new LineBasedFrameDecoder(1024))//
												.addLast(new MyMessageDecoder())//
												.addLast(new StringDecoder(CharsetUtil.UTF_8))
												.addLast(new IdleStateHandler(readerIdleTimeSeconds,
														writerIdleTimeSeconds, allIdleTimeSeconds))//
												.addLast(handlerGroup, "MyServerHandler", serverHandler);
										System.out.println("Netty服务 连接配置项");
									}
								})
								// 初始化选项
								.option(ChannelOption.SO_BACKLOG, 128)
								// 客户端连接选项
								.childOption(ChannelOption.SO_KEEPALIVE, true);
						System.out.println("Netty服务 即将启动");
						this.channel = serverBootstrap.bind(port).sync().channel();
						System.out.println("Netty服务 已启动" + index.incrementAndGet());
						isRunning = true;
					} catch (InterruptedException e) {
						System.err.println(e.getMessage());
						isRunning = false;
					}
				} else {
					System.out.println("Netty服务 已启动" + index.incrementAndGet());
				}
			}
		} else {
			System.out.println("Netty服务 已启动" + index.incrementAndGet());
		}
	}

	@PostConstruct
	public void init() {
		System.out.println("Netty服务 端口：" + port);
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Netty服务 正在关闭");
		try {
			if (this.channel != null) {
				this.channel.close();
			}
			// 释放资源
			ImmutableList.of(parentGroup, childGroup, handlerGroup).forEach(group -> {
				if (!group.isShutdown()) {
					group.shutdownGracefully().addListener(future -> {
						if (!future.isSuccess()) {
							System.out.println("Netty服务 释放资源异常：" + future.cause().getMessage());
						}
					});
				}
			});
		} catch (Exception e) {
			System.err.println("Netty服务 关闭异常：" + e.getMessage());
		}
	}

}
