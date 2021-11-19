package com.windseeker2011.netty.thread;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 简易netty服务器处理类
 * 
 * @author Weihai Li
 *
 */
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);

		ByteBuf buffer = (ByteBuf) msg;
		System.out.println("read from client>>>" + buffer.toString(CharsetUtil.UTF_8));
		buffer.release();

		String sendMsg = "connack?success=true&time=1516614508&cfgTime=1516614508&version=0.11.0&userbond=false\r\n";
		ByteBuf sendBuffer = ctx.alloc().buffer(4 * sendMsg.length());
		sendBuffer.writeBytes(sendMsg.getBytes());
		ctx.writeAndFlush(sendBuffer);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("MyServerHandler.exceptionCaught()");
		if (cause != null) cause.printStackTrace();
		if (ctx != null) ctx.close();
	}
}
