package aracket.lang;

import java.util.ArrayList;

import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;

/**
 * A object that represent procedure in racket
 * 
 * @author Athensclub
 *
 */
public abstract class RacketBaseFunction extends RacketObject {

    private RacketInterpreter racketi;

    private RacketFunction function;

    public RacketBaseFunction(RacketInterpreter interpreter) {
	racketi = interpreter;
    }

    /**
     * Get the racket interpreter containing this function
     * 
     * @return
     */
    public RacketInterpreter getInterpreter() {
	return racketi;
    }

    /**
     * Invoke this function with the given arguments and return result
     * 
     * @param args
     * @return
     */
    public abstract RacketObject invoke(RacketDictionary scope, Statement... args);

    /**
     * Get RacketFunction implementation of this racket base function.Return itself
     * if it is already racket function
     * 
     * @return
     */
    public RacketFunction asRacketFunction() {
	if (function == null) {
	    function = new RacketFunction(getInterpreter()) {

		@Override
		public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
		    ArrayList<Statement> argstm = new ArrayList<>();
		    for (RacketObject arg : args) {
			argstm.add(new RacketFunction.ObjectStatement(arg));
		    }
		    return RacketBaseFunction.this.invoke(scope, argstm.toArray(new Statement[argstm.size()]));
		}

	    };
	}
	return function;
    }

    @Override
    public String toString() {
	return "#procedure";
    }

}
