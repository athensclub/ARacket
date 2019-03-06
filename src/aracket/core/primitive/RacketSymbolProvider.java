package aracket.core.primitive;

import a10lib.compiler.token.Token;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketObject;
import aracket.lang.RacketSymbol;

public class RacketSymbolProvider extends RacketPrimitiveProvider{
    
    @Override
    public RacketObject createRacketPrimitive(Token statement) {
	if(statement.getString().length() > 1 && statement.getString().startsWith("'") || statement.getString().startsWith("’")) {
	    return new RacketSymbol(statement.getString().substring(1));
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
