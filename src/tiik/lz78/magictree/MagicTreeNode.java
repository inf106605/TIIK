package tiik.lz78.magictree;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;


class MagicTreeNode {
	
	private int size = 1;
	private final SortedMap<Byte, MagicTreeNode> map = new TreeMap<Byte, MagicTreeNode>();
	

	public int getSize() {
		return size;
	}
	
	public int addElement(final byte[] data, final int originalDataIndex, final int dataIndex, final int remainingLength, final int importance, final int depth) {
		final byte mapIndex = data[dataIndex];
		MagicTreeNode subnode = map.get(mapIndex);
		int result;
		if (remainingLength == 1) {
			if (subnode == null) {
				map.put(mapIndex, new MagicTreeNode());
				result = 1;
			} else {
				result = 0;
			}
		} else {
			if (subnode == null) {
				subnode = new MagicTreeNode();
				map.put(mapIndex, subnode);
				result = 1;
			} else {
				result = 0;
			}
			result += subnode.addElement(data, originalDataIndex, dataIndex + 1, remainingLength - 1, importance, depth + 1);
		}
		size += result;
		return result;
	}
	
	public void clear() {
		size = 1;
		map.clear();
	}
	
	public MagicTreeLeaf find(final byte[] data, final int dataIndex, final int maxLength, final int length) {
		if (length == maxLength)
			return new MagicTreeLeaf(1, length);
		final MagicTreeNode subnode = map.get(data[dataIndex]);
		if (subnode == null) {
			return new MagicTreeLeaf(1, length);
		} else {
			final MagicTreeLeaf leaf = subnode.find(data, dataIndex + 1, maxLength, length + 1);
			updateLeaf(leaf, data[dataIndex], length);
			return leaf;
		}
	}
	
	public MagicTreeLeaf get(int index, final int length) {
		if (index == 0)
			return new MagicTreeLeaf(0, length);
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
		leaf.getData()[length] = data;
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
			map.remove(bytes[depth]);
			size -= subnode.getSize();
			return subnode.getSize();
		} else {
			final int removedElements = subnode.remove(bytes, depth + 1, length, depths);
			size -= removedElements;
			return removedElements;
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
