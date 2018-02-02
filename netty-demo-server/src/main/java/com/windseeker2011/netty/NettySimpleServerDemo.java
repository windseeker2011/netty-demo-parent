package com.windseeker2011.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettySimpleServerDemo {

	public static void main(String[] args) throws Exception {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		NioEventLoopGroup parentGroup = new NioEventLoopGroup();
		NioEventLoopGroup childGroup = new NioEventLoopGroup();
		serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast("myServerHandler", new MySimpleServerHandler());

					}
				}).bind(8888).sync();
	}
}