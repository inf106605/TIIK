package tiik.lz78;


class MagicTreeLeaf {
	
	private int index;
	private final byte[] data;
	
	public MagicTreeLeaf(final int index, final int size) {
		this.index = index;
		this.data = new byte[size];
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
	public byte[] getData() {
		return data;
	}
	
}
