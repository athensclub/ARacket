package aracket.linking.std;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;
import aracket.lang.RacketFunction;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;

/**
 * A predicate function that compares 2 number
 * @author Athensclub
 *
 */
public class CompareFunction extends RacketFunction{
    
    private CompareOperation comparator;
    
    public CompareFunction(RacketInterpreter interpreter,CompareOperation comparator) {
	super(interpreter);
	this.comparator = comparator;
    }

    public static interface CompareOperation{
	
	public boolean compare(RacketNumber first,RacketNumber second);
	
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	if(args.length == 2) {
	    if(args[0] instanceof RacketNumber) {
		if(args[1] instanceof RacketNumber) {
		    return Rackets.from(comparator.compare((RacketNumber)args[0], (RacketNumber)args[1]));
		}else {
		    throw new IllegalArgumentException("Compare function expect 2 numbers found: " + args[1]);
		}
	    }else {
		throw new IllegalArgumentException("Compare function expect 2 numbers found: " + args[0]);
	    }
	}else {
	    throw new IllegalArgumentException("Compare function expected 2 arguments found: " + args.length);
	}
    }

}
