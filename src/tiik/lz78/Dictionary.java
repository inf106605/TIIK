package tiik.lz78;

import java.util.Arrays;

public interface Dictionary {
	
	public interface Entry {
		
		public int getIndex();
		public int getLength();
		public byte[] getData();
		
	}
	
	public int getSize();
	public int getMaxLength();
	public void add(final byte[] data, final int length);
	public void add(final byte[] data, final int dataIndex, final int length);
	public void removeOne();
	public Entry find(final byte[] data, final int length);
	public Entry find(final byte[] data, final int dataIndex, final int length);
	public byte[] get(final int index);
	
}
