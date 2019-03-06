package aracket.lang;

import java.util.ArrayList;

import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;

/**
 * A function that accept evaluated racket objects instead of statements
 * 
 * @author Athensclub
 *
 */
public abstract class RacketFunction extends RacketBaseFunction {

    /**
     * A statement class that is automatically evaluated into a value.Has purpose of
     * using as statement for using racket base function by passing in racket object
     * as arguments
     * 
     * @author Athensclub
     *
     */
    public static class ObjectStatement extends Statement {

	private RacketObject value;

	public ObjectStatement(RacketObject value) {
	    this.value = value;
	}

	public RacketObject value() {
	    return value;
	}

    }

    public RacketFunction(RacketInterpreter interpreter) {
	super(interpreter);
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, Statement... args) {
	ArrayList<RacketObject> fargs = new ArrayList<>();
	for (Statement stm : args) {
	    fargs.add(getInterpreter().evaluate(stm, scope));
	}
	return invoke(scope, fargs.toArray(new RacketObject[fargs.size()]));
    }
    
    @Override
    public RacketFunction asRacketFunction() {
	return this;
    }

    /**
     * invoke the function
     * 
     * @param scope
     * @param args
     */
    public abstract RacketObject invoke(RacketDictionary scope, RacketObject... args);

}
