package aracket.core.primitive;

import a10lib.compiler.Regex;
import a10lib.compiler.token.Token;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;

/**
 * A class that match statement for primitive type number
 * 
 * @author Athensclub
 *
 */
public class RacketNumberProvider extends RacketPrimitiveProvider {

    @Override
    public RacketObject createRacketPrimitive(Token statement) {
	if (Regex.matches(statement.getString(), Regex.FRACTIONABLE_NUMBER_PATTERN)) {
	    return new RacketNumber(statement.getString());
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
