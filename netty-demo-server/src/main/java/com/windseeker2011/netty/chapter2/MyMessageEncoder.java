package com.windseeker2011.netty.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class MyMessageEncoder extends MessageToByteEncoder<MyMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MyMessage msg, ByteBuf out) throws Exception {

		out.writeBytes(msg.getText().getBytes());
	}

}
