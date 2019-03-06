package aracket.lang;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import a10lib.math.Maths;
import aracket.core.Rackets;

/**
 * A class representing number in racket
 * 
 * @author Athensclub
 *
 */
public class RacketNumber extends RacketObject implements Comparable<RacketNumber> {

    public static final RacketNumber ZERO = new RacketNumber(BigDecimal.ZERO);

    public static final RacketNumber ONE = new RacketNumber(BigDecimal.valueOf(1));

    public static final RacketNumber TWO = new RacketNumber(BigDecimal.valueOf(2));

    private BigDecimal value;

    /**
     * Create a racket number with given value
     * 
     * @param val
     */
    public RacketNumber(String val) {
	if (val.contains("/")) {
	    String[] parts = val.split("/");
	    value = new BigDecimal(parts[0]).divide(new BigDecimal(parts[1]));
	} else {
	    value = new BigDecimal(val);
	}
    }

    public RacketNumber(BigDecimal val) {
	value = val;
    }

    /**
     * Get the value of this racket number
     * 
     * @return
     */
    public BigDecimal value() {
	return value;
    }

    /**
     * Add this number and other number and return result as new object
     * 
     * @param other
     * @return
     */
    public RacketNumber add(RacketNumber other) {
	return new RacketNumber(value.add(other.value));
    }

    /**
     * Subtract this number by other number and return result as new object
     * 
     * @param other
     * @return
     */
    public RacketNumber subtract(RacketNumber other) {
	return new RacketNumber(value.subtract(other.value));
    }

    /**
     * Multiply this number by other number and return result as new object
     * 
     * @param other
     * @return
     */
    public RacketNumber multiply(RacketNumber other) {
	return new RacketNumber(value.multiply(other.value));
    }

    /**
     * Divide this number by other number and return result as new object
     * 
     * @param other
     * @return
     */
    public RacketNumber divide(RacketNumber other) {
	try {
	    return new RacketNumber(value.divide(other.value));
	} catch (ArithmeticException e) {
	    return new RacketNumber(value.divide(other.value, 20+value.scale(), RoundingMode.HALF_UP));
	}
    }

    /**
     * Return absolute value of this number
     * 
     * @return
     */
    public RacketNumber abs() {
	return new RacketNumber(value.abs());
    }

    /**
     * Raise this number to the power of other number
     * 
     * @param other
     * @return
     */
    public RacketNumber pow(RacketNumber other) {
	return new RacketNumber(Maths.pow(value, other.value));
    }

    /**
     * return the square root of this number in a new racket number object
     * 
     * @return
     */
    public RacketNumber sqrt() {
	return new RacketNumber(value.sqrt(MathContext.UNLIMITED));
    }

    /**
     * Find the remainder of dividing this number by other number and return the new
     * object
     * 
     * @param other
     * @return
     */
    public RacketNumber remainder(RacketNumber other) {
	return new RacketNumber(value.remainder(other.value));
    }

    public boolean equals(RacketNumber other) {
	return compareTo(other) == 0;
    }

    @Override
    public boolean equals(Object other) {
	if (other instanceof Number) {
	    return equals(Rackets.from((Number) other));
	} else if (other instanceof RacketNumber) {
	    return equals((RacketNumber) other);
	}
	return false;
    }

    @Override
    public String toString() {
	return value.toString();
    }

    @Override
    public int compareTo(RacketNumber o) {
	return value.compareTo(o.value);
    }

}
