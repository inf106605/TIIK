package tiik.containers;

import java.util.List;

/**
 * Iterator that works good for all lists that have O(1) complexity of {@link List#get(int)}.
 */
public class UniversalIteratorForRandomAccessLists<E> implements BetterListIterator<E> {
	
	private final List<E> list;
	private int index;
	private Boolean forward = null;
	
	
	public UniversalIteratorForRandomAccessLists(final List<E> list) {
		this(list, 0);
	}
	
	public UniversalIteratorForRandomAccessLists(final List<E> list, final int index) {
		this.list = list;
		this.index = index;
	}
	
	@Override
	public void add(E e) {
		list.add(index++, e);
	}

	@Override
	public boolean hasNext() {
		return index < list.size();
	}

	@Override
	public boolean hasPrevious() {
		return index > 0;
	}

	@Override
	public E next() {
		forward = true;
		return list.get(index++);
	}

	@Override
	public int nextIndex() {
		return index;
	}

	@Override
	public E previous() {
		forward = false;
		return list.get(--index);
	}

	@Override
	public int previousIndex() {
		return index - 1;
	}

	@Override
	public void remove() {
		if (forward == null)
			throw new IllegalStateException();
		if (forward) {
			list.remove(index - 1);
			--index;
		} else {
			list.remove(index);
		}
		forward = null;
	}

	@Override
	public void set(E e) {
		list.set(index, e);
	}
	
}