package com.windseeker.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * BIO阻塞模式下{@link java.nio.channels.ServerSocketChannel}的使用
 * 
 * @author Weihai Li
 *
 */
public class NIOServerWithOutSelectorDemo {

	public static void main(String[] args) throws Exception {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		SocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
		serverSocketChannel.bind(address);
		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			Socket socket = socketChannel.socket();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(">>>" + line);
				bw.write(line + "\n");
				bw.flush();
			}
		}

	}

}
