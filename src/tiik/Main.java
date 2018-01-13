package tiik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import tiik.lz78.LZ78;
import tiik.lz78.LZ78Exception;

public class Main {

	public static void main(String[] args) {
		int argOffset;
		boolean compress = true;
		int dictLimit = 0;
		boolean summary = false;
		for (argOffset = 0; args.length > argOffset && args[argOffset].charAt(0) == '-'; ++argOffset) {
			switch(args[argOffset]) {
				case "-d":
				case "--decompress":
					compress = false;
					break;
				case "-h":
				case "--help":
					showHelp();
					return;
				case "-s":
				case "--size":
					++argOffset;
					if (args.length == argOffset) {
						System.err.println("Option '-s' needs an argument!");
						return;
					}
					try {
						dictLimit = Integer.parseInt(args[argOffset]);
					} catch (NumberFormatException e) {
						System.err.println("'" + args[argOffset] + "' is not a number!");
						return;
					}
					if (dictLimit < 0) {
						System.err.println("Dictionary size limit cannot be less than 0!");
						return;
					}
					break;
				case "--summary":
					summary = true;
					break;
				default:
					System.err.println("Unknown option '" + args[argOffset] + "'!");
					return;
			}
		}
		
		if (args.length - argOffset == 1) {
			final File file = new File(args[argOffset]);
			try (final FileInputStream inputStream = new FileInputStream(file)) {
				doTheWork(inputStream, System.out, dictLimit, compress, summary);
			} catch (FileNotFoundException e) {
				System.err.println("No such file!");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			doTheWork(System.in, System.out, dictLimit, compress, summary);
		}
	}
	
	private static void showHelp() {
		System.out.println("This programm allows to compress and decompress a file or the input stream\nusing a retarded implementation of lz78 that was written by a dumb student.\nDon't use it!");
		System.out.println();
		System.out.println("Usage:\tPROGRAM [OPTION]... [IN_FILE]");
		System.out.println("Options:");
		System.out.println("\t-d, --decompress\tDecompress.");
		System.out.println("\t-h, --help\t\tShow this info.");
		System.out.println("\t-s, --size LIMIT\tSet limit of dictionary size.\n\t\t\t\t(default: 0 - unlimited)");
		System.out.println("\t    --summary\t\tPrint summary on exit.");
	}
	
	private static void doTheWork(final InputStream inputStream, final OutputStream outputStream, final int dictLimit, final boolean compress, final boolean summary) {
		try {
			LZ78 lz78 = new LZ78(dictLimit);
			if (compress)
				lz78.compress(inputStream, outputStream);
			else
				lz78.decompress(inputStream, outputStream);
			if (summary)
				System.err.println(lz78);
		} catch (IOException | LZ78Exception e) {
			e.printStackTrace();
		}
	}
	
}
