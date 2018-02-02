package com.windseeker2011.netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MySimpleServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);

		ByteBuf buffer = (ByteBuf) msg;
		System.out.println(buffer.toString(CharsetUtil.UTF_8));
		buffer.release();

		String sendMsg = "hello";
		ByteBuf sendBuffer = ctx.alloc().buffer(4 * sendMsg.length());
		sendBuffer.writeBytes(sendMsg.getBytes());
		ctx.writeAndFlush(sendBuffer);

		// 不处理
		// ReferenceCountUtil.release(msg);
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
