package com.windseeker2011.netty.chapter2;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 简易netty服务器处理类
 * 
 * @author Weihai Li
 *
 */
@Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);

		if (!(msg instanceof MyMessage)) {
			ctx.writeAndFlush("{\"code\":400, \"message\":\"convert error\"}");
			return;
		}
		MyMessage message = (MyMessage) msg;

		System.out.println(message.getText());

		ctx.writeAndFlush(message);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("MyServerHandler.exceptionCaught()");
		if (cause != null)
			cause.printStackTrace();
		if (ctx != null)
			ctx.close();
	}
}
