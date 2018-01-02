package tiik.containers;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * It basically works as resizable cyclic-buffer.<br/>
 * It also implements interface <code>List</code> because there is no logical reason not to. (Something that Java developers are too stupid to understand so I am forced to do their job for them.)<br/>
 * <br/>
 * <b>It was never properly tested. Use it for your own responsibility.</b><br/>
 * When you will testing it, also read comments in the source code. There is a few things that may need your attention.
 */
public class ArrayDequeThatDoesNotSmellOfShit<E> implements Deque<E>, List<E> {
	
	private Object[] array;
	private int offset, size;
	
	
	public ArrayDequeThatDoesNotSmellOfShit() {
		clear();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.isEmpty())
			return false;
		reserveToHoldNMore(c.size());
		int position = offset + size - 1;
		for (final E e : c) {
			position = (position + 1) % array.length;
			array[position] = e;
		}
		size += c.size();
		return true;
	}

	@Override
	public void clear() {
		array = new Object[16];
		offset = 0;
		size = 0;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean result = true;
		for (final Object e : c)
			result &= contains(e);
		return result;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = true;
		for (final Object e : c)
			result &= remove(e);
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		final Object[] result = new Object[size];
		copyToOtherArray(result);
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
		copyToOtherArray(a);
		for (int i = size; i != array.length; ++i)
			a[i] = null;
		return a;
	}

	@Override
	public boolean add(E e) {
		addLast(e);
		return true;
	}

	@Override
	public void addFirst(E e) {
		if (size == array.length)
			reserve(array.length * 2);
		if (offset == 0)
			offset = array.length - 1;
		else
			--offset;
		array[offset] = e;
		++size;
	}

	@Override
	public void addLast(E e) {
		if (size == array.length)
			reserve(array.length * 2);
		array[(offset + size) % array.length] = e;
		++size;
	}

	@Override
	public boolean contains(Object o) {
		for (final Object e : this)
			if (o == null ? e == null : o.equals(e))
				return true;
		return false;
	}

	@Override
	public UniversalDescendingIteratorForRandomAccessLists<E> descendingIterator() {
		return new UniversalDescendingIteratorForRandomAccessLists<>(this);
	}

	@Override
	public E element() {
		return getFirst();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E getFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		return (E) array[offset];
	}

	@Override
	@SuppressWarnings("unchecked")
	public E getLast() {
		if (size == 0)
			throw new NoSuchElementException();
		return (E) array[(offset + size - 1) % array.length];
	}

	@Override
	public UniversalIteratorForRandomAccessLists<E> iterator() {
		return listIterator();
	}

	@Override
	public boolean offer(E e) {
		return offerLast(e);
	}

	@Override
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	@Override
	public E peek() {
		return peekFirst();
	}

	@Override
	@SuppressWarnings("unchecked")
	public E peekFirst() {
		if (size == 0)
			return null;
		return (E) array[offset];
	}

	@Override
	@SuppressWarnings("unchecked")
	public E peekLast() {
		if (size == 0)
			return null;;
		return (E) array[(offset + size - 1) % array.length];
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Override
	public E pollFirst() {
		if (size == 0)
			return null;
		return removeFirst();
	}

	@Override
	public E pollLast() {
		if (size == 0)
			return null;
		return removeLast();
	}

	@Override
	public E pop() {
		return removeFirst();
	}

	@Override
	public void push(E e) {
		addFirst(e);
	}

	@Override
	public E remove() {
		return removeFirst();
	}

	@Override
	public boolean remove(Object o) {
		return removeFirstOccurrence(o);
	}

	@Override
	public E removeFirst() {
		if (size == 0)
			throw new NoSuchElementException();
		@SuppressWarnings("unchecked")
		final E e = (E) array[offset];
		array[offset] = null;
		if (++offset == array.length)
			offset = 0;
		--size;
		return e;
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		return removeOccurrence(iterator(), o);
	}

	@Override
	public E removeLast() {
		if (size == 0)
			throw new NoSuchElementException();
		final int position = (offset + size - 1) % array.length;
		@SuppressWarnings("unchecked")
		final E e = (E) array[position];
		array[position] = null;
		--size;
		return e;
	}

	@Override
	public boolean removeLastOccurrence(Object o) {
		return removeOccurrence(descendingIterator(), o);
	}

	private boolean removeOccurrence(BetterIterator<E> iter, Object o) {
		final int index = find(iter, o);
		if (index != -1) {
			remove(index);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int size() {
		return size;
	}
	
	/**
	 * @return maximum number of elements that this collection can hold without resizing
	 */
	public int getCapacity() {
		return array.length;
	}
	
	/**
	 * Makes sure that this collection has at least <code>capacity</code> of reserved capacity.
	 * @param capacity
	 * @return capacity after reserving
	 */
	public int reserve(final int capacity) {
		if (capacity <= array.length) {
			return array.length;
		} else {
			Object[] newArray = new Object[capacity];
			copyToOtherArray(newArray);
			array = newArray;
			offset = 0;
			return capacity;
		}
	}
	
	private void copyToOtherArray(final Object[] otherArray) {
		int firstPartLength = array.length - offset;
		if (firstPartLength > size)
			firstPartLength = size;
		int secondPartLength = size - firstPartLength;
		System.arraycopy(array, offset, otherArray, 0, firstPartLength);
		System.arraycopy(array, 0, otherArray, firstPartLength, secondPartLength);
	}

	@Override
	public void add(int index, E element) {
		if (index > size)
			throw new IndexOutOfBoundsException();
		if (size == array.length)
			reserve(array.length * 2);
		shiftPart(index, 1);
		final int position = (offset + index) % array.length;
		array[position] = element;
		++size;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index > size)
			throw new IndexOutOfBoundsException();
		if (c.isEmpty())
			return false;
		reserveToHoldNMore(c.size());
		shiftPart(index, c.size());
		int position = (offset + index) % array.length;
		for (final E e : c) {
			array[position] = e;
			if (++position == array.length)
				position = 0;
		}
		size += c.size();
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E get(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException();
		final int position = (offset + index) % array.length;
		return (E) array[position];
	}

	@Override
	public int indexOf(Object o) {
		return find(iterator(), o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return find(descendingIterator(), o);
	}
	
	private int find(BetterIterator<E> iter, Object o) {
		while (iter.hasNext()) {
			final Object e = iter.next();
			if (o==null ? e==null : o.equals(e))
				return iter.nextIndex() - 1;
		}
		return -1;
	}

	@Override
	public UniversalIteratorForRandomAccessLists<E> listIterator() {
		return new UniversalIteratorForRandomAccessLists<>(this);
	}

	@Override
	public UniversalIteratorForRandomAccessLists<E> listIterator(int index) {
		return new UniversalIteratorForRandomAccessLists<>(this, index);
	}

	@Override
	public E remove(int index) {
		if (index == 0)
			return removeFirst();
		final E e = get(index);
		if (index + 1 != size)
			shiftPart(index + 1, -1);
		--size;
		array[(offset + size) % array.length] = null;
		// Resize the array down? (Careful with removeAll.)
		return e;
	}

	@Override
	public E set(int index, E element) {
		if (index >= size)
			throw new IndexOutOfBoundsException();
		final int position = (offset + index) % array.length;
		@SuppressWarnings("unchecked")
		final E e = (E) array[position];
		array[position] = element;
		return e;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		// It is not allowed to not support that but fuck this. I won't use it anyway and it is a lot of work to implement.
		throw new UnsupportedOperationException();
	}
	
	private void shiftPart(final int startIndex, final int shift) {
		final int partSize = size - startIndex;
		if (shift == 0 || partSize == 0)
			return;
		final int partOffset = (offset + startIndex) % array.length;
		final int relativePartOffset = (shift < 0) ? (partOffset + shift + array.length) % array.length : partOffset;
		final int relativeShift = (shift < 0) ? -shift : shift;
		final int firstPartLength = Math.min(array.length - relativePartOffset - relativeShift, partSize);
		final int secondPartLength = Math.min(partSize - firstPartLength, relativeShift);
		final int thirdPartLength = partSize - (firstPartLength + secondPartLength);
		if (shift > 0) {
			System.arraycopy(array, 0, array, shift, thirdPartLength);
			System.arraycopy(array, partOffset + firstPartLength, array, 0, secondPartLength);
			System.arraycopy(array, partOffset, array, partOffset + shift, firstPartLength);
		} else {
			System.arraycopy(array, partOffset, array, relativePartOffset, firstPartLength);
			System.arraycopy(array, 0, array, array.length + shift, secondPartLength);
			System.arraycopy(array, relativeShift, array, 0, thirdPartLength);
		}
	}

	private void reserveToHoldNMore(final int n) {
		{
			final int neededSize = size + n;
			int resultSize = array.length;
			while (resultSize < neededSize)
				resultSize *= 2;
			reserve(resultSize);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + size;
		for (final E e : this)
			result = prime * result + e.hashCode();
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
		ArrayDequeThatDoesNotSmellOfShit<?> other = (ArrayDequeThatDoesNotSmellOfShit<?>) obj;
		if (size != other.size)
			return false;
		final java.util.Iterator<?> iter1 = iterator();
		final java.util.Iterator<?> iter2 = other.iterator();
		while (true) {
			if (!iter1.hasNext())
				return true;
			final Object e1 = iter1.next();
			final Object e2 = iter2.next();
			if ((e1 == null && e2 != null) || !e1.equals(e2))
				return false;
		}
	}
	
	@Override
	public String toString() {
		boolean first = true;
		final StringBuilder result = new StringBuilder();
		result.append("[");
		for (final E e : this) {
			if (first)
				first = false;
			else
				result.append(", ");
			result.append(e);
		}
		result.append("]");
		return result.toString();
	}
	
}
