package com.windseeker.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * {@link java.nio.channels.Selector}演示用例
 * 
 * @author Weihai Li
 *
 */
public class NIOSelectorDemo {
	private static final int bufSize = 1024;

	public static void main(String[] args) throws Exception {
		Selector selector = Selector.open();
		/*
		 * 接客大厅
		 */
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(new InetSocketAddress(8888));
		/*
		 * 提供“接客”功能
		 */
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			// 是否有客人来
			selector.select();
			// 前台接客
			Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
			while (ite.hasNext()) {
				SelectionKey key = ite.next();

				if (key.isAcceptable()) {
					System.out.println("isAcceptable = true");
					SocketChannel clntChan = ((ServerSocketChannel) key.channel()).accept();
					clntChan.configureBlocking(false);
					clntChan.register(key.selector(), SelectionKey.OP_READ);
				}

				if (key.isReadable()) {
					System.out.println("isReadable = true");
					SocketChannel clntChan = (SocketChannel) key.channel();
					// clntChan.configureBlocking(false);
					ByteBuffer buffer = ByteBuffer.allocate(bufSize);
					int len = 0;
					while ((len = clntChan.read(buffer)) > 0) {
						System.out.println("read > " + new String(buffer.array()));
					}
					buffer.flip();
					byte[] bytes = new byte[buffer.limit()];
					buffer = buffer.get(bytes);
					// 注册一个写事件，注意写完数据，要取消写事件
					clntChan.register(key.selector(), SelectionKey.OP_WRITE, buffer);
				}

				if (key.isWritable() && key.isValid()) {
					System.out.println("isWritable = true");
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					buffer.flip();
					System.out.println("write > " + new String(buffer.array()));

					SocketChannel clntChan = (SocketChannel) key.channel();
					// clntChan.configureBlocking(false);
					clntChan.write(buffer);

					if (buffer.remaining() == 0) {
						buffer.clear();
						key.interestOps(SelectionKey.OP_READ);
					}
					// key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
				}

				if (key.isConnectable()) {
					System.out.println("isConnectable = true");
				}
				ite.remove();
			}
		}
	}
}
