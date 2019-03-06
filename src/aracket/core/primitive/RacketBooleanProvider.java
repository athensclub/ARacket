package aracket.core.primitive;

import a10lib.compiler.token.Token;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketBoolean;
import aracket.lang.RacketObject;

/**
 * A class that match statement for racket primitive type boolean
 * 
 * @author Athensclub
 *
 */
public class RacketBooleanProvider extends RacketPrimitiveProvider {

    @Override
    public RacketObject createRacketPrimitive(Token statement) {
	switch (statement.getString()) {
	case "#t":
	    return RacketBoolean.TRUE;
	case "#f":
	    return RacketBoolean.FALSE;
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
