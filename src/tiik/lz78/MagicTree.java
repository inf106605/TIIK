package tiik.lz78;

import java.util.ArrayList;


class MagicTree {
	
	private final MagicTreeNode mainNode = new MagicTreeNode(0);
	private ArrayList<Integer> depths = new ArrayList<>();
	private int maxDepth = 0;
	
	
	public MagicTree() {
		depths.add(1);
	}
	
	public int getSize() {
		return mainNode.getSize();
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public boolean addElement(final byte[] data, final int importance, final int dataIndex, int length) {
		int newElementCount = mainNode.addElement(data, importance, dataIndex, length);
		final boolean result = newElementCount != 0;
		if (result && maxDepth < length) {
			while (length + 1 > depths.size())
				depths.add(0);
			maxDepth = depths.size() - 1;
			for (; newElementCount != 0; --newElementCount, --length) {
				final int x = depths.get(length);
				depths.set(length, x + 1);
			}
		}
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

	public int remove(final byte[] bytes) {
		return remove(bytes, bytes.length);
	}

	public int remove(final byte[] bytes, final int length) {
		final int removedCount = mainNode.remove(bytes, 0, length, depths);
		for (int i = depths.size() - 1; depths.get(i) == 0; --i)
			depths.remove(i);
		maxDepth = depths.size();
		return removedCount;
	}

	@Override
	public String toString() {
		return mainNode.toString();
	}
	
}

