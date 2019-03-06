package aracket.linking.std;

import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketBaseFunction;
import aracket.lang.RacketBoolean;
import aracket.lang.RacketObject;

/**
 * A function that evaluate each statement. Immediately return the evaluated
 * boolean value if that matches with function wanted boolean otherwise return
 * the not of this function wanted boolean
 * 
 * @author Athensclub
 *
 */
public class BooleanFunction extends RacketBaseFunction {

    private RacketBoolean target;

    private String name;

    public BooleanFunction(RacketInterpreter interpreter, String name, RacketBoolean target) {
	super(interpreter);
	this.target = target;
	this.name = name;
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, Statement... args) {
	for (Statement stm : args) {
	    RacketObject obj = getInterpreter().evaluate(stm, scope);
	    if (obj instanceof RacketBoolean) {
		if (target.equals(obj)) {
		    return obj;
		}
	    } else {
		throw new IllegalArgumentException(name + " expected boolean argument(s) found: " + obj);
	    }
	}
	return target.not();
    }

}
