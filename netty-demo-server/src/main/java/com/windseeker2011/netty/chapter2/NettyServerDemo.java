package com.windseeker2011.netty.chapter2;

import com.windseeker2011.netty.MyMessageDecoder;
import com.windseeker2011.netty.MyMessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class NettyServerDemo {

	public static void main(String[] args) throws Exception {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		NioEventLoopGroup parentGroup = new NioEventLoopGroup();
		NioEventLoopGroup childGroup = new NioEventLoopGroup();
		serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024)).addLast(new MyMessageEncoder())
								.addLast(new MyMessageDecoder());

					}
				}).bind(8888).sync();
	}
}