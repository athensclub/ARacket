package aracket.core.primitive;

import a10lib.compiler.token.Token;
import aracket.lang.RacketObject;

/**
 * A class that matches a statement with the racket primitive
 * 
 * @author Athensclub
 *
 */
public abstract class RacketPrimitiveProvider {

    /**
     * Evaluate a statement and return a racket object of that primitive,for example
     * if this is boolean primitive provider then <code>#t</code> will be
     * returned.if that statement does not match with the primitive type,
     * {@link aracket.core.RacketInterpreter#EVAL_COMMAND} will be returned.
     * 
     * @param statement
     * @return
     */
    public abstract RacketObject createRacketPrimitive(Token statement);

}
