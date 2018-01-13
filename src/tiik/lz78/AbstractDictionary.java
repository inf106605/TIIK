package tiik.lz78;

public abstract class AbstractDictionary implements Dictionary {
	
	private int maxLengthEver = 0;
	private int resetCount = 0;
	
	
	public int getMaxLengthEver() {
		return maxLengthEver;
	}
	
	public void add(final byte[] data, final int length) {
		updateMaxLengthEver();
	}
	
	public void add(final byte[] data, final int dataIndex, final int length) {
		updateMaxLengthEver();
	}
	
	private void updateMaxLengthEver() {
		final int currentMaxLenght = getMaxLength();
		if (currentMaxLenght > maxLengthEver)
			maxLengthEver = currentMaxLenght;
	}
	
	public void reset() {
		++resetCount;
	}
	
	public int getResetCount() {
		return resetCount;
	}
	
}
