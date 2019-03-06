package aracket.linking.std;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketFunction;
import aracket.lang.RacketList;
import aracket.lang.RacketObject;

/**
 * A function that invoke a function by passing in list as arguments by converting it into seperate arguments
 * 
 * @author Athensclub
 *
 */
public class ApplyFunction extends RacketFunction{

    public ApplyFunction(RacketInterpreter interpreter) {
	super(interpreter);
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	StrictFunction.checkArgLength(args, "apply", 2);
	StrictFunction.checkArgType(args[0], RacketFunction.class, "apply");
	StrictFunction.checkArgType(args[1], RacketList.class, "apply");
	return ((RacketFunction)args[0]).invoke(new RacketDictionary(scope, getInterpreter()), ((RacketList)args[1]).toArray());
    }

}
