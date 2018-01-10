package tiik;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;

import tiik.lz78.LZ78;
import tiik.lz78.LZ78Exception;

public class Main {

	public static void main(String[] args) {
		if (args.length == 1) {
			final File file = new File(args[0]);
			try (final FileInputStream inputStream = new FileInputStream(file)) {
				testCompression(inputStream);
			} catch (FileNotFoundException e) {
				System.err.println("No such file!");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			testCompression(System.in);
		}
	}
	
	private static void testCompression(InputStream inputStream) {
		byte[] originalBytes, compressedBytes, decompressedBytes;
		{ // read data
			int readedBytes;
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] data = new byte[16384];
			try {
				while ((readedBytes = inputStream.read(data, 0, data.length)) != -1)
					buffer.write(data, 0, readedBytes);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			originalBytes = buffer.toByteArray();
		}
		{ // compress
			inputStream = new ByteArrayInputStream(originalBytes);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			LZ78 lz78 = new LZ78(originalBytes.length / 8);
			try {
				lz78.compress(inputStream, outputStream);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			compressedBytes = outputStream.toByteArray();
			System.err.println("Compresion:\n" + lz78);
			//lz78.printDebugInfo();
		}
		System.err.println();
		{ // decompress
			inputStream = new ByteArrayInputStream(compressedBytes);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			LZ78 lz78 = new LZ78();
			try {
				lz78.decompress(inputStream, outputStream);
			} catch (IOException | LZ78Exception e) {
				e.printStackTrace();
				return;
			}
			decompressedBytes = outputStream.toByteArray();
			System.err.println("Decompresion:\n" + lz78);
			//lz78.printDebugInfo();
		}
		System.err.println();
		{ // estimate Huffman
			SomeStatistics ss = new SomeStatistics(originalBytes);
			System.err.println("Estimated huffman size of plain data:\t\t" + ((int)(originalBytes.length * ss.getBinaryEntropy() / 8.0)));
			ss = new SomeStatistics(compressedBytes);
			System.err.println("Estimated huffman size of compressed data:\t" + ((int)(compressedBytes.length * ss.getBinaryEntropy() / 8.0)));
		}
		System.err.println();
		{ // compare
			if (Arrays.equals(originalBytes, decompressedBytes))
				System.err.println("Success!");
			else
				System.err.println("Fuck!");
		}
	}
	
}
