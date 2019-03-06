package aracket.core.primitive;

import a10lib.compiler.token.Token;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketKeyword;
import aracket.lang.RacketObject;

/**
 * A class that match statement for racket keyword
 * 
 * @author Athensclub
 *
 */
public class RacketKeywordProvider extends RacketPrimitiveProvider{

    @Override
    public RacketObject createRacketPrimitive(Token statement) {
	if(statement.getString().length() > 2 && statement.getString().startsWith("#:")) {
	    return new RacketKeyword(statement.getString());
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
