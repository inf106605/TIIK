package tiik.lz78;

class MagicTreeLeaf {
	
	private int index;
	private final MagicTreeNode node;
	
	public MagicTreeLeaf(final int index, final MagicTreeNode node) {
		this.index = index;
		this.node = node;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
	MagicTreeNode getNode() {
		return node;
	}
	
	public byte[] getData() {
		return node.getBytes();
	}
	
}
