package com.windseeker2011.netty.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MySimpleMessageEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub

		byte[] body = "hello client!\n".getBytes();
		out.writeInt(body.length);
		out.writeBytes(body);
	}

}
