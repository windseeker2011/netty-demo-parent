package com.windseeker2011.netty.chapter2;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 自定义消息解释器
 * 
 * @author Weihai Li
 *
 */
@Sharable
public class MyMessageDecoder extends MessageToMessageDecoder<String> {

	@Override
	protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
		MyMessage message = new MyMessage();
		if (msg != null) {
			message.setText(msg);
		}
		out.add(message);
	}

}
