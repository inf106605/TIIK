package tiik.lz78;

class MagicTreeLeaf {
	
	private int index;
	private final byte[] bytes;
	
	public MagicTreeLeaf(final int index, final int length) {
		this.index = index;
		this.bytes = new byte[length];
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
	public byte[] getData() {
		return bytes;
	}
	
}
