package tiik.lz78;

import java.util.Arrays;

final class RemovingOrderEntry implements Comparable<RemovingOrderEntry> {
	
	public int importance;
	public final byte[] bytes;
	
	
	public RemovingOrderEntry(final int importance, final byte[] bytes) {
		this.importance = importance;
		this.bytes = bytes;
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