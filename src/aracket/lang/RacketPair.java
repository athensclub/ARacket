package aracket.lang;

/**
 * A class representing pair in racket
 * 
 * @author Athensclub
 *
 */
public class RacketPair extends RacketObject {

    private RacketObject car;

    private RacketObject cdr;

    public RacketPair(RacketObject car, RacketObject cdr) {
	this.car = car;
	this.cdr = cdr;
    }

    /**
     * Get string representation of this pair without having quote symbols ' in
     * front of pair and sub pairs of this pair
     * 
     * @return
     */
    public String toStringWithoutQuote() {
	return "(" + (car instanceof RacketPair ? ((RacketPair) car).toStringWithoutQuote() : car) + " . "
		+ (cdr instanceof RacketPair ? ((RacketPair) cdr).toStringWithoutQuote() : cdr) + ")";
    }

    @Override
    public String toString() {
	return "’(" + (car instanceof RacketPair ? ((RacketPair) car).toStringWithoutQuote() : car) + " . "
		+ (cdr instanceof RacketPair ? ((RacketPair) cdr).toStringWithoutQuote() : cdr) + ")";
    }

    /**
     * Return car reference
     * 
     * @return
     */
    public RacketObject car() {
	return car;
    }

    /**
     * return cdr reference
     * 
     * @return
     */
    public RacketObject cdr() {
	return cdr;
    }

    /**
     * Get a list representation of this pair, or return reference of itself if it
     * is already a list
     * 
     * @return
     */
    public RacketList asList() {
	return new RacketList(car, new RacketList(cdr, null));
    }

}
