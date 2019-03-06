package aracket.lang;

import aracket.core.Rackets;

/**
 * A class representing boolean in racket
 * 
 * @author Athensclub
 *
 */
public class RacketBoolean extends RacketObject {

    private boolean value;

    /**
     * Constant representing boolean value true
     */
    public static final RacketBoolean TRUE = new RacketBoolean(true);

    /**
     * Constant representing boolean value false
     */
    public static final RacketBoolean FALSE = new RacketBoolean(false);

    private RacketBoolean(boolean val) {
	value = val;
    }

    public boolean equals(RacketBoolean other) {
	return value == other.value;
    }

    public RacketBoolean and(RacketBoolean other) {
	return Rackets.from(value && other.value);
    }
    
    public RacketBoolean or(RacketBoolean other) {
	return Rackets.from(value || other.value);
    }
    
    /**
     * Return the result by apply boolean operation not to this boolean
     * 
     * @return
     */
    public RacketBoolean not() {
	return Rackets.from(!value);
    }

    /**
     * Get the java value of this racket boolean
     * 
     * @return
     */
    public boolean value() {
	return value;
    }

    @Override
    public boolean equals(Object other) {
	if (other instanceof Boolean) {
	    return equals(Rackets.from((Boolean) other));
	} else if (other instanceof RacketBoolean) {
	    return equals((RacketBoolean) other);
	}
	return false;
    }

    @Override
    public String toString() {
	return value ? "#t" : "#f";
    }

}
