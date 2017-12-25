package tiik.lz78;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;


public class LZ78 {
	
	private boolean firstCall = true;
	private final int indexBytes = 4;
	private final Dictionary dictionary = new Dictionary();
	private int plainSize = 0;
	private int compressedSize = 0;
	
	
	public void compress(final InputStream inputStream, final OutputStream outputStream) throws IOException {
		if (firstCall) {
			firstCall = false;
			writeCompressionParameters(outputStream);
		}
		byte[] bytes = new byte[1];
		int length = 0;
		int offset = 0;
		while (true) {
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
					plainSize += readed;
				}
			}
			
			final Dictionary.Entry entry = dictionary.find(bytes, offset, length - 1);
			outputStream.write(toBytes(entry.index));
			outputStream.write(bytes, offset + entry.length, 1);
			compressedSize += 5;
			
			dictionary.add(bytes, offset, entry.length + 1);
			
			length -= entry.length + 1;
			offset += entry.length + 1;
		}
	}
	
	private void writeCompressionParameters(final OutputStream outputStream) throws IOException {
		//outputStream.write(new byte[]{4});
	}
	
	private byte[] toBytes(final int x) {
		int shift = (indexBytes - 1) * 8;
		final byte[] bytes = new byte[indexBytes];
		for (int mask = 0xFF << shift, i = 0; mask != 0; mask >>>= 8, ++i, shift -= 8)
			bytes[i] = (byte) ((x & mask) >>> shift);
		return bytes;
	}
	
	public void decompress(final InputStream inputStream, final OutputStream outputStream) throws IOException, LZ78Exception {
		if (firstCall) {
			firstCall = false;
			readCompressionParameters(inputStream);
		}
		final byte[] bytes = new byte[indexBytes + 1];
		while (true) {
			final int readed = inputStream.read(bytes, 0, bytes.length);
			if (readed == -1)
				break;
			else if (readed != bytes.length)
				throw new LZ78UnexpectedEndException(compressedSize, plainSize, readed, bytes.length);
			
			final int index = toInt(bytes);
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
		}
	}
	
	private void readCompressionParameters(final InputStream inpuStream) {
		//
	}
	
	private int toInt(final byte[] bytes) {
		int result = 0;
		for (int i = 0; i != indexBytes; ++i) {
			result <<= 8;
			result |= Byte.toUnsignedInt(bytes[i]);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "Dictionary size:\t" + dictionary.getSize() + "\n"
			+ "Longest entry:\t\t" + dictionary.getMaxLength() + "\n"
			+ "Plain data size:\t" + plainSize + "\n"
			+ "Compressed data size:\t" + compressedSize + " (" + String.format("%5f" , 100.0 * ((double) compressedSize) / ((double) plainSize)) + "%)";
	}
	
	public void printDebugInfo() {
		dictionary.printTree();
	}
	
}
