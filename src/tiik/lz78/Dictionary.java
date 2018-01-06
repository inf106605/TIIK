package tiik.lz78;

import java.util.Arrays;

class Dictionary {
	
	public class Entry {
		public final int index;
		public final byte[] data;
		public final int length;
		Entry(final int index, final byte[] data) {
			this.index = index;
			this.data = data;
			this.length = data.length;
		}
		@Override
		public String toString() {
			return "(" + index + ", " + Arrays.toString(data) + ")";
		}
	}
	
	
	private final MagicTree tree = new MagicTree();
	private int totalSize = 0;
	
		
	public int getSize() {
		return tree.getSize() - 1;
	}
	
	public int getMaxLength() {
		return tree.getMaxDepth();
	}
	
	public void add(final byte[] data, final int length) {
		add(data, 0, length);
	}
	
	public void add(final byte[] data, final int dataIndex, final int length) {
		totalSize += tree.addElement(data, totalSize + 1, dataIndex, length);
	}
	
	public void removeOne() {
		tree.removeLeastImportant();
	}
	
	public Entry find(final byte[] data, final int length) {
		return find(data, 0, length);
	}
	
	public Entry find(final byte[] data, final int dataIndex, final int length) {
		final MagicTreeLeaf leaf = tree.find(data, dataIndex, length);
		return new Entry(leaf.getIndex(), leaf.getData());
	}
	
	public byte[] get(final int index) {
		return tree.get(index).getData();
	}
	
	@Override
	public String toString() {
		return tree.toString();
	}
	
}
