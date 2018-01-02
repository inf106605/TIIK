package tiik.containers;

import java.util.Iterator;
import java.util.List;

public class UniversalDescendingIteratorForRandomAccessLists<E> implements Iterator<E>, BetterIterator<E> {

	private final UniversalIteratorForRandomAccessLists<E> iter;
	
	
	public UniversalDescendingIteratorForRandomAccessLists(final List<E> list) {
		this.iter = new UniversalIteratorForRandomAccessLists<>(list, list.size());
	}
	
	@Override
	public boolean hasNext() {
		return iter.hasPrevious();
	}

	@Override
	public E next() {
		return iter.previous();
	}

	@Override
	public int nextIndex() {
		return iter.previousIndex();
	}

}
