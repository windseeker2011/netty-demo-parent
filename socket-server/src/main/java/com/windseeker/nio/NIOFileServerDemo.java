package com.windseeker.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 基于NIO阻塞模式的{@link java.nio.channels.FileChannel}服务端
 * 
 * @author Weihai Li
 *
 */
public class NIOFileServerDemo {

	private static final String SRC_FILE_DIR = "D:/software/spring-tool-suite-3.8.2.RELEASE-e4.6.1-win32-x86_64.zip";
	private static final String DEST_FILE_DIR = "D:/fc.zip";
	private static final String FILE_DIR = "D:/fileChannel.txt";
	private static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 读取文件
	 * 
	 * @throws Exception
	 */
	public static void readFromFile() throws Exception {
		File file = new File(FILE_DIR);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileInputStream fis = new FileInputStream(file);
		FileChannel fileChannel = fis.getChannel();
		ByteBuffer dst = ByteBuffer.allocate(1024);
		fileChannel.read(dst);
		dst.flip();
		System.out.println(new String(dst.array(), CHARSET_UTF8));
		fis.close();
	}

	/**
	 * 写入文件
	 * 
	 * @throws Exception
	 */
	public static void writeToFile() throws Exception {
		File file = new File(FILE_DIR);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		FileChannel fileChannel = fos.getChannel();
		ByteBuffer src = ByteBuffer.allocate(1024);
		byte[] bytes = "使用FileChannel读取buffer。".getBytes(CHARSET_UTF8);
		src.put(bytes);
		src.flip();

		fileChannel.write(src);

		fos.close();
	}

	public static void transferTo(String srcDir, String destDir) throws Exception {
		long start = System.currentTimeMillis();
		File srcFile = new File(srcDir);
		if (!srcFile.exists()) {
			return;
		}
		File destFile = new File(destDir);
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileInputStream src_fis = new FileInputStream(srcFile);
		FileOutputStream dest_fos = new FileOutputStream(destFile);
		FileChannel src_fc = src_fis.getChannel();
		src_fc.transferTo(0, src_fc.size(), dest_fos.getChannel());

		src_fis.close();
		dest_fos.close();
		long end = System.currentTimeMillis();
		System.out.println("FileChannel共耗时" + (end - start) + "ms");
	}

	public static void transferTo2(String srcDir, String destDir) throws Exception {
		long start = System.currentTimeMillis();
		File srcFile = new File(srcDir);
		if (!srcFile.exists()) {
			return;
		}
		File destFile = new File(destDir);
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		FileInputStream src_fis = new FileInputStream(srcFile);
		FileOutputStream dest_fos = new FileOutputStream(destFile);
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = src_fis.read(b)) != -1) {
			dest_fos.write(b, 0, len);
			dest_fos.flush();
		}

		src_fis.close();
		dest_fos.close();
		long end = System.currentTimeMillis();
		System.out.println("File共耗时" + (end - start) + "ms");
	}

	public static void main(String[] args) throws Exception {
		// writeToFile();
		// readFromFile();
		transferTo(SRC_FILE_DIR, DEST_FILE_DIR);
		transferTo2(SRC_FILE_DIR, DEST_FILE_DIR);
	}
}
