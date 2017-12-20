
package tiik.lz78;


class MagicTree {
	
	private final MagicTreeNode mainNode = new MagicTreeNode();
	private int maxDepth = 0;
	
		
	public int getSize() {
		return mainNode.getSize();
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public void addElement(final byte[] data, final int dataIndex, final int length) {
		mainNode.addElement(data, dataIndex, length);
		maxDepth = length;
	}
	
	public MagicTreeLeaf find(final byte[] data, final int dataIndex, final int maxLength) {
		final MagicTreeLeaf leaf = mainNode.find(data, dataIndex, maxLength, 0);
		leaf.setIndex(leaf.getIndex() - 1);
		return leaf;
	}
	
	public MagicTreeLeaf get(final int index) {
		return mainNode.get(index, 0);
	}
	
}

