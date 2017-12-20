package tiik.lz78;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;


public class LZ78 {
	
	private boolean firstCall = true;
	private final Dictionary dictionary = new Dictionary();
	
	
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
				}
			}
			
			final Dictionary.Entry entry = dictionary.find(bytes, offset, length - 1);
			outputStream.write(toBytes(entry.index));
			outputStream.write(bytes, offset + entry.length, 1);
			
			dictionary.add(bytes, offset, entry.length + 1);
			
			length -= entry.length + 1;
			offset += entry.length + 1;
		}
	}
	
	public void writeCompressionParameters(final OutputStream outputStream) throws IOException {
		//outputStream.write(new byte[]{4});
	}
	
	public byte[] toBytes(final int x) {
		final int bytesNum = 4;
		final int shift = (bytesNum - 1) * 8;
		final byte[] bytes = new byte[bytesNum];
		for (int mask = 0xFF << shift, i = 0; mask != 0; mask >>>= 8, ++i)
			bytes[i] = (byte) ((x & mask) >>> shift);
		return bytes;
	}
	
}
