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
 * 简单的短连接模式（问一次一个socket）
 * 
 * @author Weihai Li
 *
 */
public class SocketShortClientDemo {

	public static void main(String[] args) throws Exception {
		System.out.println("socket client is running!");
		Scanner scanner = new Scanner(System.in);
		/*
		 * 每次问一次，都是一个新的socket
		 */
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println("keyboard: " + line);
			new Thread(new SocketShortClient(line)).start();
		}

	}

}

class SocketShortClient implements Runnable {
	private String line = null;

	public SocketShortClient(String line) {
		this.line = line;
	}

	@Override
	public void run() {
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			socket = new Socket("127.0.0.1", 8888);
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			os = socket.getOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			/*
			 * 问一次 问完，关闭输出
			 */
			bw.write(line);
			bw.flush();
			socket.shutdownOutput();// 关闭输出流
			/*
			 * 接收答案 接收完，关闭输入
			 */
			System.out.println("server: " + br.readLine());
			socket.shutdownInput();// 关闭输入流
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