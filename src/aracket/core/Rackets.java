package aracket.core;

import java.util.List;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import aracket.lang.RacketBoolean;
import aracket.lang.RacketFunction;
import aracket.lang.RacketList;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;
import aracket.lang.RacketString;

/**
 * A utilities class for using aracket library
 * 
 * @author Athensclub
 *
 */
public final class Rackets {

    private Rackets() {
    }

    /**
     * Create racket object from given java object
     * 
     * @param n
     * @return
     */
    public static RacketNumber from(Number n) {
	return new RacketNumber(n.toString());
    }

    /**
     * Create racket function from given interpreter and function
     * 
     * @param interpreter
     * @param function
     * @return
     */
    public static RacketFunction createFunction(RacketInterpreter interpreter,
	    Function<RacketObject[], RacketObject> function) {
	return new RacketFunction(interpreter) {
	    @Override
	    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
		return function.apply(args);
	    }
	};
    }

    /**
     * Create racket function from given interpreter and function and it also uses
     * scope as arguments
     * 
     * @param interpreter
     * @param function
     * @return
     */
    public static RacketFunction createScopedFunction(RacketInterpreter interpreter,
	    BiFunction<RacketDictionary,RacketObject[], RacketObject> function) {
	return new RacketFunction(interpreter) {
	    @Override
	    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
		return function.apply(scope, args);
	    }
	};
    }

    /**
     * Create racket list from given linked list
     * 
     * @param list
     * @return
     */
    public static RacketList createList(List<? super RacketObject> list) {
	ListIterator<? super RacketObject> it = list.listIterator(list.size());
	RacketList current = null;
	while (it.hasPrevious()) {
	    current = new RacketList((RacketObject) it.previous(), current);
	}
	return current;
    }

    /**
     * Create racket object from given java object
     * 
     * @param n
     * @return
     */
    public static RacketNumber from(int n) {
	return new RacketNumber(Integer.toString(n));
    }

    /**
     * Create racket object from given java object
     * 
     * @param str
     * @return
     */
    public static RacketString from(String str) {
	return new RacketString(str);
    }

    /**
     * Create racket object from given java object
     * 
     * @param b
     * @return
     */
    public static RacketBoolean from(boolean b) {
	return b ? RacketBoolean.TRUE : RacketBoolean.FALSE;
    }

}
