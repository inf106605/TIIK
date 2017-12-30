package tiik.lz78;


public class LZ78IncorrectIndexException extends LZ78Exception {
	
	private static final long serialVersionUID = -3748971843472973151L;

	public LZ78IncorrectIndexException(final int compressedPos, final int uncompressedPos, final int index) {
		super(compressedPos, uncompressedPos, "Readed a reference to index " + index + " but there is no such entry in the dictionary!");
	}
	
}
