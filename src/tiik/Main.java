package tiik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.NumberFormatException;

import tiik.lz78.LZ78;
import tiik.lz78.LZ78Exception;

public class Main {

	public static void main(String[] args) {
		int offset = 0;
		
		boolean compress = true;
		int dictLimit = 0;
		while (args.length > offset && args[offset].charAt(0) == '-') {
			switch(args[offset]) {
				case "-d":
					compress = false;
					++offset;
					break;
				case "-s":
					++offset;
					if (args.length == offset) {
						System.err.println("Option '-s' needs an argument!");
						return;
					}
					try {
						dictLimit = Integer.parseInt(args[offset]);
					} catch (NumberFormatException e) {
						System.err.println("'" + args[offset] + "' is not a number!");
						return;
					}
					if (dictLimit < 0) {
						System.err.println("Dictionary size limit cannot be less than 0!");
						return;
					}
					++offset;
					break;
				default:
					System.err.println("Unknown option '" + args[offset] + "'!");
					return;
			}
		}
		
		if (args.length - offset == 1) {
			final File file = new File(args[offset]);
			try (final FileInputStream inputStream = new FileInputStream(file)) {
				doTheWork(inputStream, System.out, dictLimit, compress);
			} catch (FileNotFoundException e) {
				System.err.println("No such file!");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			doTheWork(System.in, System.out, dictLimit, compress);
		}
	}
	
	private static void doTheWork(final InputStream inputStream, final OutputStream outputStream, final int dictLimit, final boolean compress) {
		LZ78 lz78 = new LZ78(dictLimit);
		try {
			if (compress)
				lz78.compress(inputStream, outputStream);
			else
				lz78.decompress(inputStream, outputStream);
		} catch (IOException | LZ78Exception e) {
			e.printStackTrace();
		}
	}
	
}
