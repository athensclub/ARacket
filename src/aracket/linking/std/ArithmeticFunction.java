package aracket.linking.std;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketFunction;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;

/**
 * A base class for arithmetic functions '+' '-' '*' '/'
 * 
 * @author Athensclub
 *
 */
public class ArithmeticFunction extends RacketFunction {

    /**
     * A functional interface for making arithmetic function
     * 
     * @author Athensclub
     *
     */
    @FunctionalInterface
    public static interface ArithmeticOperation {

	public RacketNumber operate(RacketNumber first, RacketNumber second);

    }

    private ArithmeticOperation operation;

    private String name;

    public ArithmeticFunction(RacketInterpreter interpreter, String name, ArithmeticOperation operation) {
	super(interpreter);
	this.operation = operation;
	this.name = name;
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	RacketNumber result = null;
	for (RacketObject obj : args) {
	    if (obj instanceof RacketNumber) {
		RacketNumber num = (RacketNumber) obj;
		if (result == null) {
		    result = num;
		} else {
		    result = operation.operate(result, num);
		}
	    } else {
		throw new IllegalArgumentException("Performed " + name + " on non-number argument: " + args);
	    }
	}
	if (result == null) {
	    throw new IllegalArgumentException("0 argument found in " + name);
	}
	return result;
    }

}
