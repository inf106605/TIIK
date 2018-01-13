package tiik.containers;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;


public abstract class UsefulPriorityQueue<E> implements Queue<E> {
	
	public static interface Updater<E> {
		boolean update(E e);
	}
	
	
	protected final Comparator<E> comparator;
	
	
	public UsefulPriorityQueue() {
		this(new Comparator<E>() {
			@Override
			@SuppressWarnings("unchecked")
			public int compare(E o1, E o2) {
				return ((Comparable<E>) o1).compareTo(o2);
			}
		});
	}
	
	public UsefulPriorityQueue(final Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	@Override
	public boolean add(final E e) {
		usefulAdd(e);
		return true;
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		if (c.isEmpty())
			return false;
		for (final E e : c)
			add(e);
		return true;
	}
	
	public abstract E get(final int index);

	@Override
	public abstract BetterIterator<E> iterator();
	
	public abstract BetterListIterator<E> listIterator();
	
	public abstract BetterListIterator<E> listIterator(final int index);
	
	public abstract E remove(final int index);
	
	public abstract int usefulAdd(final E e);
	
	public abstract int unstableUsefulAdd(final E e);
	
	public abstract int update(final int index, final Updater<E> updater);
	
	public abstract int update(final int index, final Updater<E> updater, final boolean force);
	
	public abstract int update(final int index, final E e);
	
	public abstract int update(final int index, final E e, final boolean force);
	
	public abstract int unstableUpdate(final int index, final Updater<E> updater);
	
	public abstract int unstableUpdate(final int index, final Updater<E> updater, final boolean force);
	
	public abstract int unstableUpdate(final int index, final E e);
	
	public abstract int unstableUpdate(final int index, final E e, final boolean force);
	
	public abstract BetterIterator<E> findPriority(final E serachedE);
	
}
