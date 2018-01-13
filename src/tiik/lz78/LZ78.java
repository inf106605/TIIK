package tiik.lz78;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import tiik.lz78.magictree.MagicTreeDictionary;


public class LZ78 {
	
	private int dictionarySizeLimit;
	
	private boolean firstCall = true;
	private final Dictionary dictionary = new MagicTreeDictionary();
	private int plainSize = 0;
	private int compressedSize = 0;
	
	
	public LZ78() {
		this(0);
	}
	
	public LZ78(final int dictionarySizeLimit) {
		if ((dictionarySizeLimit & (1 << 31)) != 0)
			throw new IllegalArgumentException("Maximum dictionary size limit is " + ((1 << 31) - 1) + "!");
		if (dictionarySizeLimit < 0)
			throw new IllegalArgumentException("Dictionary size limit cannot be less than 0!");
		this.dictionarySizeLimit = dictionarySizeLimit;
	}
	
	public void compress(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		if (firstCall) {
			firstCall = false;
			writeCompressionParameters(outputStream);
		}
		byte[] bytes = new byte[1];
		int length = 0;
		int offset = 0;
		while (true) {
			final int indexBytes = getIndexBytes();
			final int minBytes = dictionary.getMaxLength() + 1;
			if (minBytes > bytes.length) {
				byte[] newBytes = new byte[bytes.length * 2];
				System.arraycopy(bytes, offset, newBytes, 0, length);
				bytes = newBytes;
				offset = 0;
			} else if (minBytes + offset > bytes.length) {
				System.arraycopy(bytes, offset, bytes, 0, length);
				offset = 0;
			}
			if (length < minBytes) {
				final int readed = inputStream.read(bytes, offset + length, bytes.length - (offset + length));
				if (readed == -1) {
					if (length == 0)
						break;
				} else {
					length += readed;
				}
			}
			
			final Dictionary.Entry entry = dictionary.find(bytes, offset, length - 1);
			outputStream.write(toBytes(entry.getIndex(), indexBytes));
			outputStream.write(bytes, offset + entry.getLength(), 1);
			plainSize += entry.getLength() + 1;
			compressedSize += indexBytes + 1;
			
			dictionary.add(bytes, offset, entry.getLength() + 1);
			if (dictionarySizeLimit != 0 && dictionary.getSize() > dictionarySizeLimit)
				dictionary.reset();
			
			length -= entry.getLength() + 1;
			offset += entry.getLength() + 1;
		}
	}
	
	private void writeCompressionParameters(final OutputStream outputStream) throws IOException {
		outputStream.write(toBytes(dictionarySizeLimit, 4));
		compressedSize += 4;
	}
	
	private static byte[] toBytes(final int x, final int bytesNumber) {
		if (bytesNumber == 0)
			return new byte[0];
		int shift = (bytesNumber - 1) * 8;
		final byte[] bytes = new byte[bytesNumber];
		for (int mask = 0xFF << shift, i = 0; mask != 0; mask >>>= 8, ++i, shift -= 8)
			bytes[i] = (byte) ((x & mask) >>> shift);
		return bytes;
	}
	
	public void decompress(final InputStream inputStream, final OutputStream outputStream) throws IOException, LZ78Exception {
		if (firstCall) {
			firstCall = false;
			readCompressionParameters(inputStream);
		}
		final byte[] bytes = new byte[getMaxIndexBytes() + 1];
		while (true) {
			final int indexBytes = getIndexBytes();
			final int readed = inputStream.read(bytes, 0, indexBytes + 1);
			if (readed == -1)
				break;
			else if (readed != indexBytes + 1)
				throw new LZ78UnexpectedEndException(compressedSize, plainSize, readed, indexBytes + 1);
			
			final int index = toInt(bytes, indexBytes);
			byte[] entry;
			if (index != 0) {
				if (index > dictionary.getSize())
					throw new LZ78IncorrectIndexException(compressedSize, plainSize, index);
				entry = dictionary.get(index);
				outputStream.write(entry);
				plainSize += entry.length;
			} else {
				entry = new byte[0];
			}
			outputStream.write(bytes, indexBytes, 1);
			++plainSize;
			compressedSize += readed;
			
			//TODO optimize
			final byte[] newEntry = new byte[entry.length + 1];
			System.arraycopy(entry, 0, newEntry, 0, entry.length);
			newEntry[entry.length] = bytes[indexBytes];
			dictionary.add(newEntry, 0, newEntry.length);
			if (dictionarySizeLimit != 0 && dictionary.getSize() > dictionarySizeLimit)
				dictionary.reset();
		}
	}
	
	private void readCompressionParameters(final InputStream inputStream) throws IOException, LZ78Exception {
		final byte[] bytes = new byte[4];
		final int readed = inputStream.read(bytes, 0, 4);
		if (readed != 4)
			throw new LZ78UnexpectedEndException(compressedSize, plainSize, readed, 4);
		dictionarySizeLimit = toInt(bytes, 4);
		compressedSize += 4;
	}
	
	private static int toInt(final byte[] bytes, final int bytesNumber) {
		int result = 0;
		for (int i = 0; i != bytesNumber; ++i) {
			result <<= 8;
			result |= Byte.toUnsignedInt(bytes[i]);
		}
		return result;
	}
	
	private int getIndexBytes() {
		return getMaxBytes(dictionary.getSize());
	}
	
	private int getMaxIndexBytes() {
		return getMaxBytes(dictionarySizeLimit == 0 ? Integer.MAX_VALUE : dictionarySizeLimit);
	}
	
	private static int getMaxBytes(final int maxValue) {
		if (maxValue < (1 << 16)) {
			if (maxValue < (1 << 8)) {
				if (maxValue == 0)
					return 0;
				else
					return 1;
			} else {
				return 2;
			}
		} else {
			if (maxValue < (1 << 24))
				return 3;
			else
				return 4;
		}
	}
	
	@Override
	public String toString() {
		return "Dictionary size:\t" + dictionary.getSize() + " (max: " + (dictionarySizeLimit == 0 ? "unlimited" : dictionarySizeLimit) + ")\n"
			+ "Dictionary resets:\t" + dictionary.getResetCount() + "\n"
			+ "Longest entry:\t\t" + dictionary.getMaxLength() + "\n"
			+ "Longest entry ever:\t" + dictionary.getMaxLengthEver() + "\n"
			+ "Plain data size:\t" + plainSize + "\n"
			+ "Compressed data size:\t" + compressedSize + " (" + String.format("%.3f" , 100.0 * ((double) compressedSize) / ((double) plainSize)) + "%)";
	}
	
	public void printDebugInfo() {
		System.err.println(dictionary);
	}
	
}
