package tiik.lz78;


class MagicTreeNode {
	
	private int size = 1;
	private MagicTreeNode[] array = new MagicTreeNode[256];
	
	
	public int getSize() {
		return size;
	}
	
	public void addElement(final byte[] data, final int dataIndex, final int length) {
		++size;
		final int arrayIndex = Byte.toUnsignedInt(data[dataIndex]);
		if (length == 1) {
			array[arrayIndex] = new MagicTreeNode();
		} else {
			final MagicTreeNode subnode = array[arrayIndex];
			subnode.addElement(data, dataIndex + 1, length - 1);
		}
	}
	
	public MagicTreeLeaf find(final byte[] data, final int dataIndex, final int maxLength, final int length) {
		if (length == maxLength)
			return new MagicTreeLeaf(1, length);
		final int arrayIndex = Byte.toUnsignedInt(data[dataIndex]);
		final MagicTreeNode subnode = array[arrayIndex];
		if (subnode == null) {
			return new MagicTreeLeaf(1, length);
		} else {
			final MagicTreeLeaf leaf = subnode.find(data, dataIndex + 1, maxLength, length + 1);
			updateLeaf(leaf, arrayIndex, length);
			return leaf;
		}
	}
	
	public MagicTreeLeaf get(int index, final int length) {
		if (index == 0)
			return new MagicTreeLeaf(0, length);
		--index;
		int arrayIndex;
		for (arrayIndex = 0; ; ++arrayIndex) {
			final MagicTreeNode subnode = array[arrayIndex];
			if (subnode != null) {
				if (index - subnode.getSize() < 0)
					break;
				else
					index -= subnode.getSize();
			}
		}
		final MagicTreeLeaf leaf = array[arrayIndex].get(index, length + 1);
		updateLeaf(leaf, arrayIndex, length);
		return leaf;
	}
	
	private void updateLeaf(final MagicTreeLeaf leaf, final int arrayIndex, final int length) {
		leaf.getData()[length] = (byte) arrayIndex;
		final int subindexOffset = getSubindexOffset(arrayIndex);
		leaf.setIndex(leaf.getIndex() + subindexOffset + 1);
	}
	
	private int getSubindexOffset(int arrayIndex) {
		int offset = 0;
		for (--arrayIndex; arrayIndex != -1; --arrayIndex) {
			final MagicTreeNode subnode = array[arrayIndex];
			if (subnode != null)
				offset += subnode.getSize();
		}
		return offset;
	}
	
}
