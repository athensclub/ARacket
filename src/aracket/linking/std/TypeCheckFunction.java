package aracket.linking.std;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;
import aracket.lang.RacketFunction;
import aracket.lang.RacketObject;

/**
 * A racket function that check for a specific type
 * 
 * @author Athensclub
 *
 */
public class TypeCheckFunction extends RacketFunction {
    
    private Class<?> typeToCheck;

    public TypeCheckFunction(RacketInterpreter interpreter, Class<?> typeToCheck) {
	super(interpreter);
	this.typeToCheck = typeToCheck;
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	if(args.length == 1) {
	    return Rackets.from(typeToCheck.isAssignableFrom(args[0].getClass()));
	}else {
	    throw new IllegalArgumentException("Type check function expected 1 argument found: " + args.length);
	}
    }
    
    

}
