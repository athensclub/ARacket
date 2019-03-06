package aracket.core.primitive;

import a10lib.compiler.provider.StringProvider;
import a10lib.compiler.token.Token;
import a10lib.util.Strings;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;
import aracket.lang.RacketObject;
import aracket.lang.RacketString;

/**
 * A class that match statement for racket string
 * 
 * @author Athensclub
 *
 */
public class RacketStringProvider extends RacketPrimitiveProvider{

    @Override
    public RacketObject createRacketPrimitive(Token statement) {
	if(statement instanceof StringProvider.Token) {
	    return Rackets.from(((StringProvider.Token)statement).getLiteralValue());
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
