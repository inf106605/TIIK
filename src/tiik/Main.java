/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.IllegalFormatCodePointException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import tiik.lz78.LZ78;

/**
 *
 * @author Marcin
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		
		if (args.length == 1) {
			final File file = new File(args[0]);
			try (final FileInputStream inputStream = new FileInputStream(file)) {
				//testStatistics(inputStream);
				testCompression(inputStream);
			} catch (FileNotFoundException e) {
				System.err.println("No such file!");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			//testStatistics(System.in);
			testCompression(System.in);
		}
	}
	
	private static void testStatistics(final InputStream inputStream) {
		//byte[] bytes = "Example text".getBytes(Charset.forName("windows-1252"));
		//SomeStatistics ss = new SomeStatistics(bytes);
		
		SomeStatistics ss;
		try {
			ss = new SomeStatistics(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		final Map<Byte, Double> empiricalProbabilities = ss.getEmpiricalProbabilities();
		final Map<Byte, Double> quantitiesOfInformation = ss.getQuantitiesOfInformation();
		System.out.println("char: probability qantity-of-information");
		List<Byte> keys = new LinkedList<>(empiricalProbabilities.keySet());
	Comparator<Byte> unsignedByteComparator = new Comparator<Byte>() {
			@Override
			public int compare(final Byte byte1, final Byte byte2) {
				final int int1 = byteToUnsignedInt(byte1);
				final int int2 = byteToUnsignedInt(byte2);
				if (int1 < int2)
					return -1;
				else if (int1 > int2)
					return 1;
				else
					return 0;
			}
			private int byteToUnsignedInt(byte b) {
				return b >= 0 ? b : b + 256;
			}
		};
		Collections.sort(keys, unsignedByteComparator);
		for (byte b : keys) {
			System.out.print(String.format("0x%02X ", b));
			try {
				System.out.print(String.format("'%c'", b));
			} catch (IllegalFormatCodePointException e) {
				System.out.print("   ");
			}
			System.out.println(String.format(": %2.4f%% %1.4f", empiricalProbabilities.get(b) * 100, quantitiesOfInformation.get(b)));
		}

		System.out.println();

		System.out.println("Binary entropy: " + ss.getBinaryEntropy());
	}
	
	private static void testCompression(final InputStream inputStream) {
		LZ78 lz78 = new LZ78();
		try {
			lz78.compress(inputStream, System.out);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
}
