package tiik.lz78;

import java.util.ArrayList;

import tiik.containers.PriorityQueueThatIsActualiUsefullAsOppositeToTheStandardOne;


class MagicTree {
	
	private final PriorityQueueThatIsActualiUsefullAsOppositeToTheStandardOne<RemovingOrderEntry> removingOrder = new PriorityQueueThatIsActualiUsefullAsOppositeToTheStandardOne<>();
	private final MagicTreeNode mainNode = new MagicTreeNode(this, new byte[0], 0, 0, 0);
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
	
	PriorityQueueThatIsActualiUsefullAsOppositeToTheStandardOne<RemovingOrderEntry> getRemovingOrder() {
		return removingOrder;
	}
	
	public boolean addElement(final byte[] data, final int importance, final int dataIndex, int length) {
		int newElementCount = mainNode.addElement(data, dataIndex, dataIndex, length, importance);
		final boolean result = newElementCount != 0;
		if (result) {
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
	
	public MagicTreeLeaf find(final byte[] data) {
		return find(data, 0, data.length);
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
		final RemovingOrderEntry roe = removingOrder.element();
		final byte[] bytes = roe.bytes;
		remove(bytes);
		return roe.importance;
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

