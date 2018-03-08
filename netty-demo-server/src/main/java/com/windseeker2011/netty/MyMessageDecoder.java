package com.windseeker2011.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MyMessageDecoder extends ByteToMessageDecoder {
	
	private static final int HEADER_LENGTH = 4;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// 忽略报文长度小于4的情况
		if (in.readableBytes() < HEADER_LENGTH) {
			return;
		}
		
		in.markReaderIndex();
		// 读取正文长度
		int dataLength = in.readInt();
		// 忽略正文长度小于0的情况
		if (dataLength < 0) {
			return;
		}
		if (in.readableBytes() < dataLength) {
			//in.resetReaderIndex();
			in.release();
			return;
		}
		
		byte[] b = new byte[dataLength];
		in.readBytes(b);
		
		out.add("sucess");
	}

}
