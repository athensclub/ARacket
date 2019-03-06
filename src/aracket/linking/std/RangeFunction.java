package aracket.linking.std;

import java.math.BigDecimal;
import java.util.LinkedList;

import a10lib.util.Numbers;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;
import aracket.lang.RacketFunction;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;

/**
 * A function that accept range end and create a list of numbers beginning with
 * 0 and increment by 1 with the given end times
 * 
 * @author Athensclub
 *
 */
public class RangeFunction extends RacketFunction {

    public RangeFunction(RacketInterpreter interpreter) {
	super(interpreter);
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	StrictFunction.checkArgLength(args, "range", 1);
	StrictFunction.checkArgType(args[0], RacketNumber.class, "range");
	BigDecimal number = ((RacketNumber) args[0]).value();
	if (number.compareTo(BigDecimal.ZERO) >= 0) {
	    if (Numbers.isInteger(number)) {
		LinkedList<RacketObject> list = new LinkedList<>();
		for (BigDecimal i = BigDecimal.ZERO; i.compareTo(number) < 0; i = i.add(BigDecimal.ONE)) {
		    list.add(new RacketNumber(i));
		}
		return Rackets.createList(list);
	    } else {
		throw new IllegalArgumentException("range function with end that is not integer: " + number);
	    }
	} else {
	    throw new IllegalArgumentException("range function with end < 0: " + number);
	}
    }

}
