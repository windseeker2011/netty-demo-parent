package com.windseeker2011.netty.chapter2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Netty服务
 * 
 * @author Weihai Li
 *
 */
public class NettyServerDemo {

	public static void main(String[] args) throws Exception {
		NettyService service = new NettyService();
		// 启动100个Netty服务
		ExecutorService es = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 50; i++) {
			es.submit(new Runnable() {
				@Override
				public void run() {
					service.run();
				}
			});
		}
	}
}