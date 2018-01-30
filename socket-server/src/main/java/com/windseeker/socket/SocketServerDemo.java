package com.windseeker.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于BIO阻塞模式的socket服务端
 * 
 * @author Weihai Li
 *
 */
public class SocketServerDemo {

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(8888);
		System.out.println("socket server is running...");
		while (true) {
			Socket socket = serverSocket.accept();
			new Thread(new SocketHandlerServer(socket)).start();
		}
	}

}

class SocketHandlerServer implements Runnable {
	private Socket socket = null;

	public SocketHandlerServer(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(socket.getRemoteSocketAddress() + " - " + line);
				/*
				 * 忽略心跳包
				 */
				if ("keep-alive".equals(line)) {
					continue;
				}
				/*
				 * 数据包处理
				 */
				bw.write(line + "\n");
				bw.flush();
				/*
				 * 断开标识，关闭长连接
				 */
				if ("exit".equals(line)) {
					socket.shutdownInput();// 关闭输入流
				}
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
