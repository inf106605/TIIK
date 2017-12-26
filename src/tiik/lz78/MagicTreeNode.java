package tiik.lz78;

import java.util.SortedMap;
import java.util.TreeMap;


class MagicTreeNode {
	
	private int size = 1;
	private final SortedMap<Byte, MagicTreeNode> map = new TreeMap<Byte, MagicTreeNode>();
	
	
	public int getSize() {
		return size;
	}
	
	public boolean addElement(final byte[] data, final int dataIndex, final int length) {
		final MagicTreeNode subnode = map.get(data[dataIndex]);
		boolean result;
		if (length == 1) {
			if (subnode == null) {
				map.put(data[dataIndex], new MagicTreeNode());
				result = true;
			} else {
				result = false;
			}
		} else {
			result = subnode.addElement(data, dataIndex + 1, length - 1);
		}
		if (result)
			++size;
		return result;
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
		for (SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet()) {
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
		for (SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet()) {
			if (entry.getKey() >= data)
				break;
			offset += entry.getValue().getSize();
		}
		return offset;
	}
	
	public void print(final String name) {
		System.err.println("\"" + name + "\" (" + size + ")" );
		for (SortedMap.Entry<Byte, MagicTreeNode> entry : map.entrySet())
			entry.getValue().print(name + ((char)(byte)entry.getKey()));
	}
	
}
