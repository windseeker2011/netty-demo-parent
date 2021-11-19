package com.windseeker2011.netty.thread.single;

import com.windseeker2011.netty.thread.SimpleServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 单线程模型
 * @author Weihai Li
 *
 */
public class SingleThreadNettyServerDemo {

	public static void main(String[] args) throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup(1);
		ServerBootstrap server = new ServerBootstrap();
		server.group(group).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new LineBasedFrameDecoder(1024)).addLast("myServerHandler",
						new SimpleServerHandler());

			}
		}).bind(8888).sync();
	}

}
