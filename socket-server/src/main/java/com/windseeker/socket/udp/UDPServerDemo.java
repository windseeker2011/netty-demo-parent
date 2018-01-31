package com.windseeker.socket.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 基于BIO阻塞模式、UDP协议的服务端
 * 
 * @author Weihai Li
 *
 */
public class UDPServerDemo {
	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		try {
			/*
			 * 1.创建指定端口的数据包socket
			 */
			datagramSocket = new DatagramSocket(8888);
			/*
			 * 2.接收数据包
			 */
			byte[] receiveBytes = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
			/*
			 * 3.发送数据包
			 */
			while (true) {
				datagramSocket.receive(receivePacket);
				System.out.println(">>>from client :" + new String(receivePacket.getData()));
				datagramSocket.send(receivePacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (datagramSocket != null)
				datagramSocket.close();
		}

	}
}
