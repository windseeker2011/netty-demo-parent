package com.windseeker2011.netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 简易netty客户端处理类
 * 
 * @author Weihai Li
 *
 */
public class MySimpleClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);

		ByteBuf buffer = (ByteBuf) msg;
		System.out.println("read from server>>>" + buffer.toString(CharsetUtil.UTF_8));
		buffer.release();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("MyServerHandler.exceptionCaught()");
		if (cause != null)
			cause.printStackTrace();
		if (ctx != null)
			ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		String sendMsg = "hello server!";
		System.out.println("write to server>>>" + sendMsg);
		ByteBuf sendBuffer = ctx.alloc().buffer(4 * sendMsg.length());
		sendBuffer.writeBytes(sendMsg.getBytes());
		ctx.writeAndFlush(sendBuffer);
	}
}
