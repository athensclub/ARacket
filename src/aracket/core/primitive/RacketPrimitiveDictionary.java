package aracket.core.primitive;

import java.util.ArrayList;

import a10lib.compiler.token.Token;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketObject;

/**
 * A dictionary containing all racket primitive data type
 * 
 * @author Athensclub
 *
 */
public class RacketPrimitiveDictionary {

    private ArrayList<RacketPrimitiveProvider> primitives = new ArrayList<>();

    public RacketPrimitiveDictionary() {
	primitives.add(new RacketNumberProvider());
	primitives.add(new RacketStringProvider());
	primitives.add(new RacketBooleanProvider());
	primitives.add(new RacketSymbolProvider());
	primitives.add(new RacketKeywordProvider());
    }

    /**
     * Evaluate a statement and return a racket object of that primitive.if that
     * statement does not match with the primitive type,
     * {@link aracket.core.RacketInterpreter#EVAL_COMMAND} will be returned.
     * 
     * @param statement
     * @return
     */
    public RacketObject createPrimitive(Token statement) {
	RacketObject temp = RacketInterpreter.EVAL_COMMAND;
	for(RacketPrimitiveProvider provider : primitives) {
	    if((temp = provider.createRacketPrimitive(statement)) != RacketInterpreter.EVAL_COMMAND) {
		return temp;
	    }
	}
	return RacketInterpreter.EVAL_COMMAND;
    }

}
