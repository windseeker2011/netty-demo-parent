package com.windseeker2011.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 基于BIO阻塞模式的socket客户端<br/>
 * 简单的长连接模式（全部问完一个socket）
 * 
 * @author Weihai Li
 *
 */
public class SocketLongClientDemo {

	public static void main(String[] args) throws Exception {
		System.out.println("socket client is running!");
		/*
		 * 全部问完一个socket
		 */
		new Thread(new SocketLongClient()).start();
	}

}

class SocketLongClient implements Runnable {
	Socket socket = null;
	InputStream is = null;
	OutputStream os = null;
	BufferedReader br = null;
	BufferedWriter bw = null;

	/*
	 * 心跳机制
	 */
	public void sendHeartBeat() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
						os.write("keep-alive\n".getBytes());
						os.flush();
						// bw.write("keep-alive");
						// bw.flush();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void run() {
		try {
			/*
			 * 1.断线重连机制
			 */
			if (socket == null || socket.isClosed()) {
				socket = new Socket("127.0.0.1", 8888);
				os = socket.getOutputStream();
			}
			/*
			 * 2.保持心跳机制
			 */
			sendHeartBeat();
			/*
			 * 3.
			 */
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println("keyboard: " + line);
				/*
				 * 问一次，写结尾符
				 */
				bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				bw.write(line + "\n");
				bw.flush();
				/*
				 * 接收答案
				 */
				is = socket.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				System.out.println("server: " + br.readLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (bw != null)
					bw.close();
				if (os != null)
					os.close();
				if (br != null)
					br.close();
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}