package com.windseeker2011.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * 基于BIO阻塞模式、UDP协议的客户端
 * 
 * @author Weihai Li
 *
 */
public class UDPClientDemo {
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			/*
			 * 1.创建无状态的数据包socket
			 */
			datagramSocket = new DatagramSocket();
			/*
			 * 2.指定IP和端口
			 */
			InetAddress address = InetAddress.getByName("127.0.0.1");
			int port = 8888;
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				/*
				 * 3.往指定IP和端口发数据包
				 */
				byte[] sendBytes = line.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, address, port);
				datagramSocket.send(sendPacket);
				System.out.println(">>>send to server:" + new String(sendBytes));
				/*
				 * 4.从指定IP和端口收数据包
				 */
				byte[] receiveBytes = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length, address, port);
				datagramSocket.receive(receivePacket);
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
			/*
			 * 5.关闭资源
			 */
			if (datagramSocket != null)
				datagramSocket.close();
		}
	}
}
