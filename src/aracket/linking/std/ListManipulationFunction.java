package aracket.linking.std;

import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketBaseFunction;
import aracket.lang.RacketFunction;
import aracket.lang.RacketList;
import aracket.lang.RacketObject;

/**
 * A function that take function then use that function to utilize it with a
 * list in some way
 * 
 * @author Athensclub
 *
 */
public class ListManipulationFunction extends RacketBaseFunction {

    /**
     * A class that use function and list to work
     * 
     * @author Athensclub
     *
     */
    @FunctionalInterface
    public interface ListManipulation {

	/**
	 * Invoke this function
	 */
	public RacketObject apply(RacketDictionary scope, RacketList list, RacketFunction function);
   
    }

    private ListManipulation implementation;

    private String name;

    public ListManipulationFunction(RacketInterpreter interpreter, String name, ListManipulation implementation) {
	super(interpreter);
	this.implementation = implementation;
	this.name = name;
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, Statement... args) {
	if(args.length == 2) {
	    RacketObject first = getInterpreter().evaluate(args[0], scope);
	    if(first instanceof RacketBaseFunction) {
		RacketObject second = getInterpreter().evaluate(args[1],scope);
		if(second instanceof RacketList) {
		    return implementation.apply(scope, (RacketList)second, ((RacketBaseFunction) first).asRacketFunction());
		}else {
		    throw new IllegalArgumentException(name + " expected second argument as list found: " + second);
		}
	    }else {
		throw new IllegalArgumentException(name + " expected first argument as procedure found: " + first);
	    }
	}else {
	    throw new IllegalArgumentException(name + " expected 2 arguments found: " + args.length);
	}
    }

}
