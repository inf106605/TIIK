package tiik.containers;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;


/**
 * Do you believe that standard Java implementation of priority queue doesn't have a way to check at which position the new element was added?<br/>
 * I do, because Java is a <b>piece of shit</b>! Why do I have work with that language :-(<br/>
 * <br/>
 * "Java is fully OOP language, it was designed for this. It has mechanics that ensures that you are programming OOP."
 * Yes, definitely! Ensures it by forcing you to write class for every fucking function you write.
 * Even if the class will have one static method and nothing more, you can't just write the method, you have to create a new class.
 * It's a shame that the whole OOP support in Java ends here.
 * It is so OOP friendly anymore when you try to force encapsulation.
 * (Not that encapsulation has any importance for OOP or even is base concept behind it. No, the only thing you need is creating a new class for the tiniest utility function you write.)<br/>
 * Look, I'm trying to make this queue to ensure that elements inside it are sorted by priority.
 * Every setter function and any other function that is supposed to change the contents of the queue does that.
 * Too bad that even a fucking getter allows to change the content of the queue and it is no way to prevent that.
 * Lack of support for constant references literally violates encapsulation.<br/>
 * &lt;/rant&gt;
 */
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
