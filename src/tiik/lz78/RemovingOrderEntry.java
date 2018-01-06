package tiik.lz78;

import java.util.Arrays;

final class RemovingOrderEntry implements Comparable<RemovingOrderEntry> {
	
	public int importance;
	public final byte[] bytes;
	public final MagicTreeNode node;
	
	
	public RemovingOrderEntry(final int importance, final byte[] bytes, final MagicTreeNode node) {
		this.importance = importance;
		this.bytes = bytes;
		this.node = node;
	}
	
	@Override
	public int compareTo(RemovingOrderEntry o) {
		return Integer.compare(importance, o.importance);
	}
	
	@Override
	public String toString() {
		return "(" + importance + ", " + Arrays.toString(bytes) + ")";
	}
	
}