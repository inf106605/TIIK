package tiik.containers;

import java.util.Iterator;

/**
 * Iterator which also has method <code>nextIndex()</code>.
 */
public interface BetterIterator<E> extends Iterator<E> {
	
	public int nextIndex();
	
}
