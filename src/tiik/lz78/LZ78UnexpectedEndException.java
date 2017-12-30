package tiik.lz78;


public class LZ78UnexpectedEndException extends LZ78Exception {
	
	private static final long serialVersionUID = 2690535942793568697L;

	public LZ78UnexpectedEndException(final int compressedPos, final int uncompressedPos, final int readedBytes, final int expectedBytes) {
		super(compressedPos, uncompressedPos, "Expected " + expectedBytes + " bytes but the stream has ended after " + readedBytes + " bytes!");
	}
	
}
