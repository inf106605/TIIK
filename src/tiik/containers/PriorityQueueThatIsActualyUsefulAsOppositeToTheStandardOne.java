package tiik.containers;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Queue;


/**
 * This implementaion isn't very fast but it al least works.
 */
public class PriorityQueueThatIsActualyUsefulAsOppositeToTheStandardOne<E> extends UsefulPriorityQueue<E> {
	
	private final ArrayDequeThatDoesNotSmellOfShit<E> container = new ArrayDequeThatDoesNotSmellOfShit<E>();
	
	
	public PriorityQueueThatIsActualyUsefulAsOppositeToTheStandardOne() {
		super();
	}
	
	public PriorityQueueThatIsActualyUsefulAsOppositeToTheStandardOne(final Comparator<E> comparator) {
		super(comparator);
	}

	@Override
	public void clear() {
		container.clear();
	}

	@Override
	public boolean contains(final Object o) {
		return container.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return container.containsAll(c);
	}
	
	@Override
	public E element() {
		return container.element();
	}
	
	@Override
	public E get(final int index) {
		return container.get(index);
	}

	@Override
	public boolean isEmpty() {
		return container.isEmpty();
	}

	@Override
	public BetterIterator<E> iterator() {
		return container.iterator();
	}
	
	@Override
	public BetterListIterator<E> listIterator() {
		return container.listIterator();
	}
	
	@Override
	public BetterListIterator<E> listIterator(final int index) {
		return container.listIterator(index);
	}
	
	@Override
	public boolean offer(final E e) {
		return add(e);
	}
	
	@Override
	public E peek() {
		return container.peek();
	}
	
	@Override
	public E poll() {
		return container.poll();
	}
	
	@Override
	public E remove() {
		return container.remove();
	}
	
	@Override
	public E remove(final int index) {
		return container.remove(index);
	}

	@Override
	public boolean remove(final Object o) {
		return container.remove(o);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return container.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return container.retainAll(c);
	}

	@Override
	public int size() {
		return container.size();
	}

	@Override
	public Object[] toArray() {
		return container.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return container.toArray(a);
	}
	
	@Override
	public int usefulAdd(final E e) {
		final int index = findIndex(e);
		container.add(index, e);
		return index;
	}
	
	@Override
	public int unstableUsefulAdd(final E e) {
		final int index = unstableFindIndex(e);
		container.add(index, e);
		return index;
	}
	
	private int findIndex(final E newE) {
		if (container.isEmpty())
			return 0;
		final List<Integer> deltas = makeDeltas();
	    final Iterator<Integer> iter = deltas.iterator();
		int i = iter.next() - 1;
	    while (true) {
	        final E currentE = container.get(i);
	        final int compareCurrent = comparator.compare(newE, currentE);
	        final E nextE = (i == container.size() - 1) ? null : container.get(i + 1);
	        final int compareNext = (nextE == null) ? -1 : comparator.compare(newE, newE);
			if (compareCurrent >= 0 && compareNext < 0) {
	            return i + 1;
	        } else if (!iter.hasNext()) {
	        	if (compareCurrent < 0)
	        		return i;
	        	else
	        		return i + 1;
	        } else {
	            if (compareCurrent < 0)
	                i -= iter.next();
	            else
	                i += iter.next();
	        }
	    }
	}
	
	private int unstableFindIndex(final E newE) {
		if (container.isEmpty())
			return 0;
		final List<Integer> deltas = makeDeltas();
	    final Iterator<Integer> iter = deltas.iterator();
		int i = iter.next() - 1;
	    while (true) {
	        final E e = container.get(i);
	        final int compare = comparator.compare(newE, e);
			if (compare == 0) {
	            return i;
	        } else if (!iter.hasNext()) {
	        	if (compare < 0)
	        		return i;
	        	else
	        		return i + 1;
	        } else {
	            if (compare < 0)
	                i -= iter.next();
	            else
	                i += iter.next();
	        }
	    }
	}
	
	private List<Integer> makeDeltas() {
		final int size = container.size();
		final LinkedList<Integer> deltas = new LinkedList<>();
		int power = 1;
	    while (true) {
	        final int half = power;
	        power <<= 1;
	        final int delta = (size + half) / power;
	        if (delta == 0)
	        	return deltas;
	        deltas.addLast(delta);
	    }
	}
	
	@Override
	public int update(final int index, final Updater<E> updater) {
		return update(index, updater, false);
	}
	
	@Override
	public int update(final int index, final Updater<E> updater, final boolean force) {
		final E e = get(index);
		if (!updater.update(e))
			return index;
		return update(index, e, force);
	}
	
	@Override
	public int update(final int index, final E e) {
		return update(index, e, false);
	}
	
	@Override
	public int update(final int index, final E e, final boolean force) {
		if (!force && !needsUpdate(index, e))
			return index;
		remove(index);
		return usefulAdd(e);
	}
	
	@Override
	public int unstableUpdate(final int index, final Updater<E> updater) {
		return unstableUpdate(index, updater, false);
	}
	
	@Override
	public int unstableUpdate(final int index, final Updater<E> updater, final boolean force) {
		final E e = get(index);
		final boolean result = updater.update(e);
		if (result)
			return unstableUpdate(index, e, force);
		else
			return index;
	}
	
	@Override
	public int unstableUpdate(final int index, final E e) {
		return unstableUpdate(index, e, false);
	}
	
	@Override
	public int unstableUpdate(final int index, final E e, final boolean force) {
		if (!force && !needsUpdate(index, e))
			return index;
		remove(index);
		return unstableUsefulAdd(e);
	}

	private boolean needsUpdate(final int index, final E e) {
		final E previous = (index == 0) ? null : get(index - 1);
		final E next = (index + 1 == container.size()) ? null : get(index + 1);
		final int comparedPrevious = (previous == null) ? 1 : comparator.compare(e, previous);
		final int comparedNext = (next == null) ? -1 : comparator.compare(e, next);
		return comparedPrevious < 0 || comparedNext > 0;
	}
	
	@Override
	public BetterIterator<E> findPriority(final E serachedE) {
		if (container.isEmpty())
			throw new NoSuchElementException();
		final List<Integer> deltas = makeDeltas();
	    final Iterator<Integer> iter = deltas.iterator();
		int i = iter.next() - 1;
	    while (true) {
	        final E e = container.get(i);
	        final int compare = comparator.compare(serachedE, e);
			if (compare == 0) {
	            BetterListIterator<E> listIter = container.listIterator(i);
	            while (listIter.hasPrevious()) {
		            if (comparator.compare(serachedE, listIter.previous()) != 0)
		            	return listIter;
	            }
	            return listIter;
	        } else if (!iter.hasNext()) {
	        	throw new NoSuchElementException();
	        } else {
	            if (compare < 0)
	                i -= iter.next();
	            else
	                i += iter.next();
	        }
	    }
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparator == null) ? 0 : comparator.hashCode());
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriorityQueueThatIsActualyUsefulAsOppositeToTheStandardOne<?> other = (PriorityQueueThatIsActualyUsefulAsOppositeToTheStandardOne<?>) obj;
		if (comparator == null) {
			if (other.comparator != null)
				return false;
		} else if (!comparator.equals(other.comparator))
			return false;
		if (container == null) {
			if (other.container != null)
				return false;
		} else if (!container.equals(other.container))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return container.toString();
	}
	
}
