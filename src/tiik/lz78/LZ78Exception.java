package tiik.lz78;


public class LZ78Exception extends Exception {
	
	final private int compressedPos, uncompressedPos;
	
	public LZ78Exception(final int compressedPos, final int uncompressedPos, final String message) {
		super("At byte " + compressedPos + " of compressed data (" + uncompressedPos + " byte of plain data): " + message);
		this.compressedPos = compressedPos;
		this.uncompressedPos = uncompressedPos;
	}
	
}
