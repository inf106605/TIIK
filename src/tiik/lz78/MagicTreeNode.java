package tiik.lz78;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;


class MagicTreeNode {
	
	private final MagicTree tree;
	private int size = 1;
	private int removingOrderIndex;
	private final SortedMap<Byte, MagicTreeNode> map = new TreeMap<Byte, MagicTreeNode>();
	
	
	public MagicTreeNode(final MagicTree tree, final byte[] data, final int dataIndex, final int length, final int importance) {
		this.tree = tree;
		final byte[] bytes = new byte[length];
		System.arraycopy(data, dataIndex, bytes, 0, length);
		removingOrderIndex = tree.getRemovingOrder().usefulAdd(new RemovingOrderEntry(importance, bytes));
		updateRemovingOrderIndices(removingOrderIndex + 1, tree.getRemovingOrder().size());
	}

	public int getSize() {
		return size;
	}
	
	int getRemovingOrderIndex() {
		return removingOrderIndex;
	}
	
	public byte[] getBytes() {
		return tree.getRemovingOrder().get(removingOrderIndex).bytes;
	}
	
	public int addElement(final byte[] data, final int originalDataIndex, final int dataIndex, final int length, final int importance) {
		final byte mapIndex = data[dataIndex];
		MagicTreeNode subnode = map.get(mapIndex);
		int result;
		if (length == 1) {
			if (subnode == null) {
				map.put(mapIndex, new MagicTreeNode(tree, data, originalDataIndex, dataIndex - originalDataIndex + 1, importance));
				result = 1;
			} else {
				result = 0;
			}
		} else {
			if (subnode == null) {
				subnode = new MagicTreeNode(tree, data, originalDataIndex, dataIndex - originalDataIndex + 1, importance);
				map.put(mapIndex, subnode);
				result = 1;
			} else {
				result = 0;
			}
			result += subnode.addElement(data, originalDataIndex, dataIndex + 1, length - 1, importance);
		}
		size += result;
		final RemovingOrderEntry removingOrderEntry = tree.getRemovingOrder().get(removingOrderIndex);
		if (removingOrderEntry.importance <= importance) {
			final int oldRemovingOrderIndex = removingOrderIndex;
			tree.getRemovingOrder().remove(removingOrderIndex);
			removingOrderEntry.importance = importance;
			removingOrderIndex = tree.getRemovingOrder().usefulAdd(removingOrderEntry);
			updateRemovingOrderIndices(oldRemovingOrderIndex, removingOrderIndex);
		}
		return result;
	}
	
	public MagicTreeLeaf find(final byte[] data, final int dataIndex, final int maxLength, final int length) {
		if (length == maxLength)
			return new MagicTreeLeaf(1, this);
		final MagicTreeNode subnode = map.get(data[dataIndex]);
		if (subnode == null) {
			return new MagicTreeLeaf(1, this);
		} else {
			final MagicTreeLeaf leaf = subnode.find(data, dataIndex + 1, maxLength, length + 1);
			updateLeaf(leaf, data[dataIndex], length);
			return leaf;
		}
	}
	
	public MagicTreeLeaf get(int index, final int length) {
		if (index == 0)
			return new MagicTreeLeaf(0, this);
		--index;
		for (final SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet()) {
			if (index - entry.getValue().getSize() < 0) {
				final MagicTreeLeaf leaf = entry.getValue().get(index, length + 1);
				updateLeaf(leaf, entry.getKey(), length);
				return leaf;
			} else {
				index -= entry.getValue().getSize();
			}
		}
		return null;
	}
	
	private void updateLeaf(final MagicTreeLeaf leaf, final byte data, final int length) {
		final int subindexOffset = getSubindexOffset(data);
		leaf.setIndex(leaf.getIndex() + subindexOffset + 1);
	}
	
	private int getSubindexOffset(byte data) {
		int offset = 0;
		for (final SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet()) {
			if (entry.getKey() >= data)
				break;
			offset += entry.getValue().getSize();
		}
		return offset;
	}
	
	public int remove(final byte[] bytes, final int depth, final int length, final ArrayList<Integer> depths) {
		final MagicTreeNode subnode = map.get(bytes[depth]);
		if (length - 1 == depth) {
			subnode.removeFromOtherCollections(depths, depth);
			map.remove(bytes[depth]);
			size -= subnode.getSize();
			return subnode.getSize();
		} else {
			final int removedElements = subnode.remove(bytes, depth + 1, length, depths);
			size -= removedElements;
			return removedElements;
		}
	}
	
	private void removeFromOtherCollections(final ArrayList<Integer> depths, final int depth) {
		for (final MagicTreeNode subnode : map.values())
			subnode.removeFromOtherCollections(depths, depth + 1);
		final int x = depths.get(depth);
		depths.set(depth, x - 1);
		tree.getRemovingOrder().remove(removingOrderIndex);
		updateRemovingOrderIndices(0, tree.getRemovingOrder().size());
	}
	
	private void updateRemovingOrderIndices(final int begin, final int end) {
		for (int i = begin; i != end; ++i) {
			final RemovingOrderEntry roe = tree.getRemovingOrder().get(i);
			final MagicTreeNode node = tree.find(roe.bytes).getNode();
			node.removingOrderIndex = i;
		}
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	
	private String toString(final String name) {
		String result = "\"" + name + "\" (" + size + ")";
		for (final SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet())
			result += "\n" + entry.getValue().toString(name + ((char)(byte)entry.getKey()));
		return result;
	}
	
}
