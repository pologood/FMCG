package net.wit.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class GZipUtils {

	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];
		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);
		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		decompresser.end();
		return output;
	}

	public static byte[] compress(byte[] value, int offset, int length, int compressionLevel) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length);

		Deflater compressor = new Deflater();

		try {
			compressor.setLevel(compressionLevel); // 将当前压缩级别设置为指定值。
			compressor.setInput(value, offset, length);
			compressor.finish(); // 调用时，指示压缩应当以输入缓冲区的当前内容结尾。

			// Compress the data
			final byte[] buf = new byte[1024];
			while (!compressor.finished()) {
				// 如果已到达压缩数据输出流的结尾，则返回 true。
				int count = compressor.deflate(buf);
				// 使用压缩数据填充指定缓冲区。
				bos.write(buf, 0, count);
			}
		} finally {
			compressor.end(); // 关闭解压缩器并放弃所有未处理的输入。
		}

		return bos.toByteArray();
	}

	public static byte[] compress(byte[] value, int offset, int length) {
		return compress(value, offset, length, Deflater.BEST_SPEED);
		// 最佳压缩的压缩级别
	}

	public static byte[] compress(byte[] value) {
		return compress(value, 0, value.length, Deflater.BEST_SPEED);
	}

	public static void main(String[] args) throws Exception {
		
		testCompress();
		testDeCompress();

	}
	
	private static void testCompress() throws Exception{
		File file = new File("E:\\rzico\\doc\\result");

		int len = (int) file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] src = new byte[len];
		fis.read(src, 0, len);
		byte[] out = decompress(src);
		FileOutputStream fos = new FileOutputStream(new File("E:\\rzico\\doc\\result.java.txt"));
		fos.write(out);
		fos.flush();
		fos.close();
	}
	
	private static void testDeCompress() throws Exception{
		File file = new File("E:\\rzico\\doc\\Test.java.txt");

		int len = (int) file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] src = new byte[len];
		fis.read(src, 0, len);
		byte[] out = compress(src);
		FileOutputStream fos = new FileOutputStream(new File("E:\\rzico\\doc\\result_new"));
		fos.write(out);
		fos.flush();
		fos.close();
	}
}
