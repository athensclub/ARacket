package aracket.core.command;

import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketBoolean;
import aracket.lang.RacketObject;

/**
 * A class representing racket command if
 * 
 * @author Athensclub
 *
 */
public class IfCommand extends RacketCommand {

    public IfCommand(RacketInterpreter interpreter) {
	super("if", interpreter);
    }

    @Override
    public RacketObject evaluate(Block command, RacketDictionary scope) {
	if (command.getSubStatement().size() == 4) {
	    Statement ifTrueBlock = command.getSubStatement().get(2);
	    Statement elseBlock = command.getSubStatement().get(3);
	    RacketObject condition = getInterpreter().evaluate(command.getSubStatement().get(1), scope);
	    if (condition instanceof RacketBoolean) {
		if (condition.equals(RacketBoolean.TRUE)) {
		    return getInterpreter().evaluate(ifTrueBlock, scope);
		} else {
		    return getInterpreter().evaluate(elseBlock, scope);
		}
	    } else {
		throw new IllegalArgumentException("Using if command with non-boolean condition: " + condition);
	    }
	} else {
	    throw new IllegalArgumentException(
		    "If command expected 3 arguments found: " + (command.getSubStatement().size() - 1));
	}
    }

}
