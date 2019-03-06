package aracket.lang;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import a10lib.util.ref.IntReference;
import a10lib.util.ref.Reference;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;

/**
 * A class representing racket list
 * 
 * @author Athensclub
 *
 */
public class RacketList extends RacketPair {

    public RacketList(RacketObject car, RacketList cdr) {
	super(car, cdr);
    }

    @Override
    public RacketList asList() {
	return this;
    }

    /**
     * Convert this racket list to java list
     * 
     * @return
     */
    public List<RacketObject> toJavaList() {
	LinkedList<RacketObject> result = new LinkedList<>();
	forEach(obj -> result.add(obj));
	return result;
    }

    /**
     * Convert this list to array
     * 
     * @return
     */
    public RacketObject[] toArray() {
	List<RacketObject> jlist = toJavaList();
	return jlist.toArray(new RacketObject[jlist.size()]);
    }

    /**
     * Apply consumer function to every object in this list. Iterate through this
     * list in order of first element to last element.
     * 
     * @param consumer
     */
    public void forEach(Consumer<RacketObject> consumer) {
	RacketList current = this;
	while (true) {
	    consumer.accept(current.car() == null ? RacketInterpreter.NULL : current.car());
	    RacketList next = (RacketList) current.cdr();
	    if (next == null) {
		break;
	    } else {
		current = next;
	    }
	}
    }

    /**
     * filter all the list with the given predicate and return a new list
     * 
     * @param predicate
     * @return
     */
    public RacketList filter(Predicate<RacketObject> predicate) {
	LinkedList<RacketObject> result = new LinkedList<>();
	forEach(obj -> {
	    if (predicate.test(obj)) {
		result.add(obj);
	    }
	});
	return Rackets.createList(result);
    }

    /**
     * Create a list with the same element as this list. But reverse order
     * 
     * @return
     */
    public RacketList reverse() {
	Reference<RacketList> current = new Reference<>();
	forEach(obj -> current.value = new RacketList(obj, current.value));
	return current.value;
    }

    /**
     * Create a new list that is a list of all the elements of this list appended by
     * the all the elements of the other list
     * 
     * @param other
     * @return
     */
    public RacketList append(RacketList other) {
	Reference<RacketList> result = new Reference<RacketList>(other);
	reverse().forEach(obj -> result.value = new RacketList(obj, result.value));
	return result.value;
    }

    @Override
    public RacketList clone() {
	return Rackets.createList(toJavaList());
    }

    /**
     * Get the length of this list
     * 
     * @return
     */
    public int length() {
	IntReference count = new IntReference(0);
	forEach(obj -> count.value++);
	return count.value;
    }

    /**
     * map this list to a new list with given function
     * 
     * @param function
     */
    public RacketList map(Function<RacketObject, RacketObject> function) {
	LinkedList<RacketObject> result = new LinkedList<>();
	forEach(obj -> result.add(function.apply(obj)));
	return Rackets.createList(result);
    }

    @Override
    public String toStringWithoutQuote() {
	StringBuilder result = new StringBuilder("(");
	Reference<Boolean> space = new Reference<Boolean>(false);
	forEach(obj -> {
	    if (!space.value) {
		space.value = true;
	    } else {
		result.append(' ');
	    }
	    result.append(obj instanceof RacketPair ? ((RacketPair) obj).toStringWithoutQuote() : obj);
	});
	return result.append(')').toString();
    }

    @Override
    public String toString() {
	StringBuilder result = new StringBuilder("’(");
	Reference<Boolean> space = new Reference<Boolean>(false);
	forEach(obj -> {
	    if (!space.value) {
		space.value = true;
	    } else {
		result.append(' ');
	    }
	    result.append(obj instanceof RacketPair ? ((RacketPair) obj).toStringWithoutQuote() : obj);
	});
	return result.append(')').toString();
    }

}
