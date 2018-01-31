package com.windseeker2011.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * 基于BIO阻塞模式、UDP协议的聊天客户端
 * 
 * @author Weihai Li
 *
 */
public class MulticastUDPClientDemo {
	// TODO 可以实现简易内网聊天室功能
	public static void main(String[] args) {
		MulticastSocket multicastSocket = null;
		InetAddress address = null;
		try {
			multicastSocket = new MulticastSocket();
			/*
			 * 1.指定多点广播地址
			 */
			address = InetAddress.getByName("224.0.0.2");
			int port = 8888;
			// 加入多点广播群组
			multicastSocket.joinGroup(address);
			// 设置本MulticastSocket发送的数据报会被回送到自身
			multicastSocket.setLoopbackMode(false);
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				/*
				 * 3.往指定IP和端口发数据包
				 */
				byte[] sendBytes = line.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, address, port);
				multicastSocket.send(sendPacket);
				System.out.println(">>>send to server:" + new String(sendBytes));
				/*
				 * 4.从指定IP和端口收数据包
				 */
				byte[] receiveBytes = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length, address, port);
				multicastSocket.receive(receivePacket);
				System.out.println(">>>receive from server:" + new String(receivePacket.getData()));
				/*
				 * 停止发送标识
				 */
				if ("exit".equals(line)) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (address != null)
					multicastSocket.leaveGroup(address);// 离开组
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			/*
			 * 5.关闭资源
			 */
			if (multicastSocket != null)
				multicastSocket.close();
		}
	}
}
