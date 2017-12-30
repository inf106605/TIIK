
package tiik.lz78;


class MagicTree {
	
	private final MagicTreeNode mainNode = new MagicTreeNode(0);
	private int maxDepth = 0;
	
	
	public int getSize() {
		return mainNode.getSize();
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public boolean addElement(final byte[] data, final int importance, final int dataIndex, final int length) {
		final boolean result = mainNode.addElement(data, importance, dataIndex, length);
		if (result && maxDepth < length)
			maxDepth = length;
		return result;
	}
	
	public MagicTreeLeaf find(final byte[] data, final int dataIndex, final int maxLength) {
		final MagicTreeLeaf leaf = mainNode.find(data, dataIndex, maxLength, 0);
		leaf.setIndex(leaf.getIndex() - 1);
		return leaf;
	}
	
	public MagicTreeLeaf get(final int index) {
		return mainNode.get(index, 0);
	}
	
	public int removeLeastImportant() {
		final byte[] bytes = new byte[maxDepth];
		final int[] length = new int[1];
		final int minImportance = mainNode.findLeastImportant(Integer.MAX_VALUE, bytes, 0, length);
		remove(bytes, length[0]);
		return minImportance;
	}

	public void remove(final byte[] bytes) {
		remove(bytes, bytes.length);
	}

	public void remove(final byte[] bytes, final int length) {
		mainNode.remove(bytes, 0, length);
		recalculateMaxDepth();
	}
	
	private void recalculateMaxDepth() {
		maxDepth = mainNode.calculateMaxDepth() - 1;
	}

	@Override
	public String toString() {
		return mainNode.toString();
	}
	
}

